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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * this class is currently experimental.
 * Looking at it too closely might make your eyes bleed....
 */

@SupportedAnnotationTypes("com.ngandroid.lib.annotations.NgModel")
public class NgProcessor extends AbstractProcessor {

    private static final String MODEL_INTERFACE_CLASS = "com.ngandroid.lib.ng.Model";
    private static final String MODEL_METHOD_INTERFACE_CLASS = "com.ngandroid.lib.ng.ModelMethod";
    private static final String SCOPE_INTERFACE_CLASS = "com.ngandroid.lib.ng.Scope";

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

    public static final class SourceField {
        private final String name, typeName;
        private String getter, setter;

        public SourceField(String name, String typeName) {
            this.name = name;
            this.typeName = typeName;
        }

        public String getName(){
            return name;
        }

        public String getTypeName(){
            return typeName;
        }

        public String getGetter() { return getter; }

        public String getSetter() { return setter; }

        public void setSetter(String setter) {
            this.setter = setter;
        }

        public void setGetter(String getter) {
            this.getter = getter;
        }
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
}