/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.attrcompiler.ExpressionBuilder;
import com.github.davityle.ngprocessor.attrcompiler.getters.Getter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * this class is currently experimental.
 * Looking at it too closely might make your eyes bleed....
 *
 * Parse xml
 * valid syntax of attributes
 * map models to scopes
 * check to see if each xml file containing ngattributes matches at least 1 scope
 *
 */

@SupportedAnnotationTypes("com.ngandroid.lib.annotations.NgModel")
public class NgProcessor extends AbstractProcessor {

    private static final Pattern NAME_SPACE_PATTERN = Pattern.compile("xmlns:(.+)=\"http://schemas.android.com/apk/res-auto\"");
    private static final Pattern ID_ATTR_PATTERN = Pattern.compile("android:id=\"@\\+id/(.+)\"");
    private static final String NAMESPACE_ATTRIBUTE_REG = "%s:(.+)=\"(.+)\"";

    private static final String MODEL_APPENDAGE = "$$NgModel";
    private static final String SCOPE_APPENDAGE = "$$NgScope";


    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.size() == 0)
            return false;
        System.out.println(":NgAndroid:processing");

        List<File> layoutDirs = findLayouts();

        for(File f : layoutDirs){
            for(File kid : f.listFiles()){
                if(kid.getName().endsWith(".xml")){
                    System.out.println(kid);
                    Document doc;
                    try {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        doc = db.parse(kid);
                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        error(null, e.getMessage());
                        continue;
                    }

                    NodeList children = doc.getChildNodes();
                    String nameSpace = getNameSpace(children);
                    if(nameSpace != null){
                        String pattern = String.format(NAMESPACE_ATTRIBUTE_REG, nameSpace);
                        Pattern nameSpaceAttributePattern = Pattern.compile(pattern);
                        List<XmlNode> nodeList = new ArrayList<>();
                        getNgAttrNodes(doc, nameSpaceAttributePattern, nodeList, kid.getName());
                        for(XmlNode xmlNode : nodeList){
                            for(XmlAttribute xmlAttribute : xmlNode.getAttributes()){
                                try {
                                    Getter getter = new ExpressionBuilder(xmlAttribute.getValue()).build();
                                    System.out.println(getter.getSource());
                                }catch(RuntimeException e){
                                    error(null,
                                            "Layout file '%s' has an invalid attribute '%s' in view '%s' with value '%s' because '%s'",
                                            kid,
                                            xmlAttribute.getName(),
                                            xmlNode.getId(),
                                            xmlAttribute.getValue(),
                                            e.getMessage()
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<String, List<Element>> scopeBuilderMap = new LinkedHashMap<>();
        Map<String, Element> modelBuilderMap = new LinkedHashMap<>();

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for(Element element : elements){

                Set<Modifier> modifiers =  element.getModifiers();
                if(modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)){
                    error(element, "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
                    continue;
                }

                TypeMirror fieldType = element.asType();
                String fieldTypeName = fieldType.toString();
                modelBuilderMap.put(fieldTypeName, element);

                Element scopeClass = element.getEnclosingElement();
                String packageName = getPackageName((TypeElement) scopeClass);
                String className = getClassName((TypeElement) scopeClass, packageName);
                String scopeName = className + SCOPE_APPENDAGE;
                String key = packageName + "." + scopeName;
                List<Element> els = scopeBuilderMap.get(key);
                if(els == null){
                    els = new ArrayList<>();
                    scopeBuilderMap.put(key, els);
                }
                els.add(element);
            }
        }

        Properties props = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        Set<Map.Entry<String, Element>> models = modelBuilderMap.entrySet();
        for(Map.Entry<String, Element> model : models){
            Element element = model.getValue();

            TypeMirror fieldType = element.asType();
            String fieldTypeName = fieldType.toString();
            int periodindex = fieldTypeName.lastIndexOf('.');
            TypeElement typeElement = (TypeElement) typeUtils.asElement(fieldType);
            String packageName = getPackageName((TypeElement) typeUtils.asElement(fieldType));

            List<SourceField> fields = new ArrayList<>();

            String modelName = fieldTypeName.substring(periodindex + 1);

            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            boolean isInterface = typeElement.getKind().isInterface();

            for(int index = 0; index < enclosedElements.size(); index++){
                Element enclosedElement = enclosedElements.get(index);
                if(isSetter(enclosedElement)) {
                    ExecutableElement setter = (ExecutableElement) enclosedElement;
                    if (!returnsVoid(setter)) {
                        error(element, "Setter '%s' must not return a value", element.toString());
                        continue;
                    }
                    Set<Modifier> modifiers = setter.getModifiers();
                    if(modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)){
                        error(element, "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
                        continue;
                    }
                    String fName = setter.getSimpleName().toString().substring(3).toLowerCase();
                    TypeMirror typeMirror = setter.getParameters().get(0).asType();
                    String type = typeMirror.toString();
                    SourceField sourceField = new SourceField(fName, type);
                    sourceField.setSetter(setter.getSimpleName().toString());
                    // O(n^2) is the best
                    for(Element possGetter : enclosedElements) {
                        if(isGetterForField(possGetter, fName, typeMirror.getKind())){
                            sourceField.setGetter(possGetter.getSimpleName().toString());
                            break;
                        }
                    }
                    fields.add(sourceField);
                }
            }

            VelocityContext vc = new VelocityContext();

            vc.put("simpleClassName", modelName);
            vc.put("className", packageName + '.' + modelName);
            vc.put("packageName", packageName);
            vc.put("isInterface", isInterface);
            vc.put("fields", fields);

            Template vt = ve.getTemplate("templates/ngmodel.vm");

            try {
                JavaFileObject jfo = filer.createSourceFile(packageName + "." + modelName + MODEL_APPENDAGE, element);
                Writer writer = jfo.openWriter();
                vt.merge(vc, writer);
                writer.flush();
                writer.close();
            }catch (IOException e){
                error(element, e.getMessage());
            }

        }


        Set<Map.Entry<String, List<Element>>> entries = scopeBuilderMap.entrySet();
        for(Map.Entry<String, List<Element>> entry : entries){
            List<Element> elements = entry.getValue();
            Element scopeClass = elements.get(0).getEnclosingElement();
            String packageName = getPackageName((TypeElement) scopeClass);
            String className = getClassName((TypeElement) scopeClass, packageName);

            List<SourceField> fields = new ArrayList<>();
            for(Element element : elements){
                Name fieldName = element.getSimpleName();
                TypeMirror fieldType = element.asType();
                fields.add(new SourceField(fieldName.toString(), fieldType.toString()));
            }
            VelocityContext vc = new VelocityContext();

            vc.put("simpleClassName", className);
            vc.put("className", packageName + '.' + className);
            vc.put("packageName", packageName);
            vc.put("fields", fields);

            Template vt = ve.getTemplate("templates/ngscope.vm");


            try {
                Element[] els = elements.toArray(new Element[elements.size() + 1]);
                els[elements.size()] = scopeClass;
                JavaFileObject jfo = filer.createSourceFile(packageName + "." + className + SCOPE_APPENDAGE, els);
                Writer writer = jfo.openWriter();
                vt.merge(vc, writer);
                writer.flush();
                writer.close();
            }catch (IOException e){
                error(scopeClass, e.getMessage());
            }
        }
        System.out.println(":NgAndroid:successful");
        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private static boolean isSetter(Element elem){
        return elem != null && ExecutableElement.class.isInstance(elem)
                && elem.getKind() == ElementKind.METHOD
                && elem.getSimpleName().toString().startsWith("set")
                && ((ExecutableElement) elem).getParameters().size() == 1;
    }

    private static boolean isGetterForField(Element elem, String field, TypeKind typeKind){
        return elem != null && ExecutableElement.class.isInstance(elem)
                && elem.getKind() == ElementKind.METHOD
                && elem.getSimpleName().toString().toLowerCase().equals("get" + field)
                && ((ExecutableElement) elem).getReturnType().getKind() == typeKind
                && ((ExecutableElement) elem).getParameters().size() == 0;
    }

    private static boolean returnsVoid(ExecutableElement method){
        TypeMirror type = method.getReturnType();
        return (type != null && type.getKind() == TypeKind.VOID);
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private static boolean hasAnnotationWithName(Element element, String simpleName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String annotationName = mirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void getNgAttrNodes(Document doc, Pattern attributePattern, List<XmlNode> ngAttrNodes, String fileName){
        NodeList children = doc.getChildNodes();
        for(int index = 0; index < children.getLength(); index++) {
            getNgAttrNodes(children.item(index), attributePattern, ngAttrNodes, fileName);
        }
    }

    private void getNgAttrNodes(Node n, Pattern attributePattern, List<XmlNode> ngAttrNodes, String fileName){
        if(n == null || !n.hasChildNodes())
            return;

        NodeList nodes = n.getChildNodes();
        for(int index = 0; index < nodes.getLength(); index++) {
            Node childNode = nodes.item(index);
            NamedNodeMap nodeMap = childNode.getAttributes();
            if(nodeMap != null){
                List<XmlAttribute> attributeList = null;
                String id = null;
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Node node = nodeMap.item(j);
                    Matcher idMatcher = ID_ATTR_PATTERN.matcher(node.toString());

                    if(idMatcher.matches()){
                        id = idMatcher.group(1);
                       continue;
                    }

                    Matcher matcher = attributePattern.matcher(node.toString());
                    if(matcher.matches()){
                        if(attributeList == null)
                            attributeList = new ArrayList<>();
                        attributeList.add(new XmlAttribute(matcher.group(1), matcher.group(2)));
                    }

                }
                if(attributeList != null){
                    if(id == null){
                        error(null, "xml attributes '%s' in node '%s' in layout file '%s' need an id", attributeList.toString(), childNode.toString(), fileName);
                    }else {
                        ngAttrNodes.add(new XmlNode(id, attributeList));
                    }
                }
            }
            getNgAttrNodes(childNode, attributePattern, ngAttrNodes, fileName);
        }
    }

    private String getNameSpace(NodeList nodes){
        for(int index = 0; index < nodes.getLength(); index++) {
            NamedNodeMap nodeMap = nodes.item(index).getAttributes();
            if(nodeMap != null){
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Matcher matcher = NAME_SPACE_PATTERN.matcher(nodeMap.item(j).toString());
                    if(matcher.matches()){
                        return matcher.group(1);
                    }
                }
            }
        }
        for(int index = 0; index < nodes.getLength(); index++) {
            NodeList nodeList = nodes.item(index).getChildNodes();
            if(nodeList != null){
                String namespace = getNameSpace(nodeList);
                if(namespace != null)
                    return namespace;
            }
        }
        return null;
    }

    private List<File> findLayouts(){
        File root = new File(".");
        List<File> files = new ArrayList<>();
        findLayouts(root, files);
        return files;
    }

    private void findLayouts(File f, List<File> files){
        File[] kids = f.listFiles();
        if(kids != null) {
            for (File file : kids) {
                String name = file.getName();
                if (file.isDirectory() && !name.equals("build") && !name.equals("bin")) {
                    if (file.getName().equals("layout")) {
                        files.add(file);
                    } else {
                        findLayouts(file, files);
                    }
                }
            }
        }
    }
}