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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.element.VariableElement;
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

                TypeMirror fieldType = element.asType();
                String fieldTypeName = fieldType.toString();
                modelBuilderMap.put(fieldTypeName, element);

                Element scopeClass = element.getEnclosingElement();
                String packageName = getPackageName((TypeElement) scopeClass);
                String className = getClassName((TypeElement) scopeClass, packageName);
                String scopeName = className + "$$NgScope";
                String key = packageName + "." + scopeName;
                List<Element> els = scopeBuilderMap.get(key);
                if(els == null){
                    els = new ArrayList<>();
                    scopeBuilderMap.put(key, els);
                }
                els.add(element);
            }
        }
        Set<Map.Entry<String, Element>> models = modelBuilderMap.entrySet();
        for(Map.Entry<String, Element> model : models){
            Element element = model.getValue();
            Name fieldName = element.getSimpleName();
            element.getModifiers().contains(Modifier.PRIVATE);
            TypeMirror fieldType = element.asType();
            String fieldTypeName = fieldType.toString();
            // will break if there isn't a package....
            int periodindex = fieldTypeName.lastIndexOf('.');
            TypeElement typeElement = (TypeElement) typeUtils.asElement(fieldType);
            String packageName = getPackageName((TypeElement) typeUtils.asElement(fieldType));
            String modelPackage;
            if(periodindex != -1)
                modelPackage = "package " + packageName + ";";
            else
                modelPackage = "";

            String modelName = fieldTypeName.substring(periodindex + 1);

            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            boolean isInterface = typeElement.getKind().isInterface();

            StringBuilder builder = new StringBuilder(modelPackage)
                    .append("\n\npublic class ")
                    .append(modelName)
                    .append("$$NgModel ");
            if(isInterface)
                builder.append("implements ");
            else
                builder.append("extends ");
            builder.append(fieldTypeName)
                    .append(" {\n\n");

            System.out.println(enclosedElements.size());

            if(isInterface){
                for(Element enclosedElement : enclosedElements){
                    if(isMethod(enclosedElement)) {
                        ExecutableElement exElement = (ExecutableElement) enclosedElement;
                        if (!returnsVoid(exElement)) {
                            builder.append("\tprivate ")
                                    .append(exElement.getReturnType().toString())
                                    .append(' ')
                                    .append(exElement.getSimpleName().toString().replace("get", "").toLowerCase())
                                    .append(";\n");

                        }
                    }
                }
                builder.append('\n');
            }

            for(Element enclosedElement : enclosedElements){
                if(isMethod(enclosedElement)){
                    ExecutableElement exElement = (ExecutableElement) enclosedElement;
                    List<? extends VariableElement> parameters = exElement.getParameters();

                    String fName = exElement.getSimpleName().toString().replace("get", "").replace("set","").toLowerCase();

                    builder.append("\tpublic ")
                            .append(exElement.getReturnType().toString())
                            .append(' ')
                            .append(exElement.getSimpleName().toString())
                            .append('(');
                    for(int index = 0; index < parameters.size(); index++){
                        VariableElement parameter = parameters.get(index);
                        builder.append(parameter.asType().toString())
                                .append(' ')
                                .append(fName);
                        if(index != parameters.size() - 1)
                            builder.append(',');
                    }
                    builder.append(") {\n\t\t");
                    if(!returnsVoid(exElement)){
                        if(isInterface) {
                            builder.append("return ")
                                    .append(fName)
                                    .append(';');
                        }else {
                            builder.append("return super.")
                                    .append(exElement.getSimpleName().toString())
                                    .append("();");
                        }
                    }else{
                        if(isInterface) {
                            builder.append("this.")
                                    .append(fName)
                                    .append(" = ")
                                    .append(fName)
                                    .append(';');
                        }else{
                            builder.append("super.")
                                    .append(exElement.getSimpleName().toString())
                                    .append('(')
                                    .append(fName)
                                    .append(");");
                        }
                    }
                    builder.append("\n\t}\n\n");
                }else{
                    System.out.println("Not Executable method");
                }
            }
            builder.append("}");

            try {
                JavaFileObject jfo = filer.createSourceFile(packageName + "." + modelName + "$$NgModel", element);
                Writer writer = jfo.openWriter();
                writer.write(builder.toString());
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
            String scopeName = className + "$$NgScope";
            StringBuilder fields = new StringBuilder();
            for(Element element : elements){
                Name fieldName = element.getSimpleName();
                element.getModifiers().contains(Modifier.PRIVATE);

                TypeMirror fieldType = element.asType();
                String fieldTypeName = fieldType.toString();
                // will break if there isn't a package....
                String fieldTypePackage = fieldTypeName.substring(0, fieldTypeName.lastIndexOf('.'));
                fields.append("\tpublic ")
                        .append(fieldType.toString())
                        .append(" get")
                        .append(fieldName)
                        .append("(){\n\t\treturn scope.")
                        .append(fieldName)
                        .append(";\n\t}\n\n");
            }
            try {
                String s = new StringBuilder("package ")
                        .append(packageName)
                        .append(";\n\n")
                        .append("public class ")
                        .append(scopeName)
                        .append(" <T extends ")
                        .append(packageName)
                        .append(".")
                        .append(className)
                        .append("> {\n\n\tprivate T scope;\n")
                        .append("\tpublic ")
                        .append(scopeName)
                        .append("(T scope){\n\t\tthis.scope = scope;\n\t}\n\n")
                        .append(fields.toString())
                        .append("}").toString();
                Element[] els = elements.toArray(new Element[elements.size() + 1]);
                els[elements.size()] = scopeClass;
                JavaFileObject jfo = filer.createSourceFile(packageName + "." + scopeName, els);
                Writer writer = jfo.openWriter();
                writer.write(s);
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

    private static boolean isMethod(Element elem){
        return elem != null && ExecutableElement.class.isInstance(elem) && elem.getKind() == ElementKind.METHOD;
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
