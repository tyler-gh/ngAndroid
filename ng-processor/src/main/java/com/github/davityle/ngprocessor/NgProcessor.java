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

    private static final String MODEL_INTERFACE_CLASS = "com.ngandroid.lib.ng.Model";
    private static final String MODEL_METHOD_INTERFACE_CLASS = "com.ngandroid.lib.ng.ModelMethod";
    private static final String SCOPE_INTERFACE_CLASS = "com.ngandroid.lib.ng.Scope";


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
            if(isInterface) {
                builder.append("implements ");
            } else {
                builder.append("extends ");
            }
            builder.append(fieldTypeName);

            if(isInterface){
                builder.append(", ");
            }else{
                builder.append(" implements ");
            }

            builder.append(MODEL_INTERFACE_CLASS).append(" {\n\n");

            StringBuilder switchBuilder = new StringBuilder("\t\tswitch(field){\n");
            StringBuilder getSwitchBuilder = new StringBuilder("\t\tswitch(field){\n");
            StringBuilder setSwitchBuilder = new StringBuilder("\t\tswitch(field){\n");

            for(Element enclosedElement : enclosedElements){
                if(isMethod(enclosedElement)) {
                    ExecutableElement exElement = (ExecutableElement) enclosedElement;
                    String fName = exElement.getSimpleName().toString().replace("get", "").replace("set", "").toLowerCase();
                    String varname;
                    if(isInterface)
                        varname = fName + "_";
                    else
                        varname = fName;
                    if (!returnsVoid(exElement)) {
//                        String type = exElement.getReturnType().toString();
//                        if(isInterface) {
//                            builder.append("\tprivate ")
//                                    .append(type)
//                                    .append(' ')
//                                    .append(fName)
//                                    .append(";\n");
//                        }
//                        builder.append("\tprivate java.util.List<com.ngandroid.lib.ng.ModelMethod> ")
//                                .append(fName)
//                                .append("Observers;\n");
                        switchBuilder.append("\t\t\tcase \"")
                                .append(fName)
                                .append("\":\n\t\t\t\t")
                                .append("if(")
                                .append(fName)
                                .append("Observers == null){\n\t\t\t\t\t")
                                .append(fName)
                                .append("Observers = new java.util.ArrayList<>();\n\t\t\t\t}\n\t\t\t\t")
                                .append(fName)
                                .append("Observers.add(modelMethod);\n\t\t\t\tSystem.out.println(\"Added observer\");\n\t\t\t\tbreak;\n");
                        getSwitchBuilder.append("\t\t\tcase \"")
                                .append(fName)
                                .append("\":\n\t\t\t\treturn ")
                                .append(varname)
                                .append(";\n");
                    }else{
                        String type = exElement.getParameters().get(0).asType().toString();
                        if(isInterface) {
                            builder.append("\tprivate ")
                                    .append(type)
                                    .append(' ')
                                    .append(varname)
                                    .append(";\n");
                        }
                        builder.append("\tprivate java.util.List<com.ngandroid.lib.ng.ModelMethod> ")
                                .append(fName)
                                .append("Observers;\n");

                        setSwitchBuilder.append("\t\t\tcase \"")
                                .append(fName)
                                .append("\":\n\t\t\t\t")
                                .append(exElement.getSimpleName())
                                .append("((")
                                .append(type)
                                .append(") value);\n\t\t\t\treturn;\n");
                    }
                }
            }
            builder.append('\n');

            switchBuilder.append("\t\t}\n");
            getSwitchBuilder.append("\t\t}\n\t\tthrow new com.ngandroid.lib.exceptions.NgException(\"Field '\" + field + \"' was not found in \" + getClass().getSimpleName());\n");
            setSwitchBuilder.append("\t\t}\n\t\tthrow new com.ngandroid.lib.exceptions.NgException(\"Field '\" + field + \"' was not found in \" + getClass().getSimpleName());\n");


            builder.append("\tpublic void addObserver(String field, com.ngandroid.lib.ng.ModelMethod modelMethod){\n")
                    .append(switchBuilder)
                    .append("\t}\n\n")
                    .append("\tpublic Object getValue(String field){\n")
                    .append(getSwitchBuilder)
                    .append("\t}\n\n")
                    .append("\tpublic void setValue(String field, Object value){\n")
                    .append(setSwitchBuilder)
                    .append("\t}\n\n");

            for(Element enclosedElement : enclosedElements){
                if(isMethod(enclosedElement)){
                    ExecutableElement exElement = (ExecutableElement) enclosedElement;
                    if(!returnsVoid(exElement) && !isInterface)
                        continue;
                    List<? extends VariableElement> parameters = exElement.getParameters();

                    String fName = exElement.getSimpleName().toString().replace("get", "").replace("set","").toLowerCase();
                    String varname;
                    if(isInterface)
                        varname = fName + "_";
                    else
                        varname = fName;
                    builder.append("\tpublic ")
                            .append(exElement.getReturnType().toString())
                            .append(' ')
                            .append(exElement.getSimpleName().toString())
                            .append('(');
                    for(int index = 0; index < parameters.size(); index++){
                        VariableElement parameter = parameters.get(index);
                        builder.append(parameter.asType().toString())
                                .append(' ')
                                .append(varname);
                        if(index != parameters.size() - 1)
                            builder.append(',');
                    }
                    builder.append(") {\n\t\t");
                    if(!returnsVoid(exElement)){
                        if(isInterface) {
                            builder.append("return ")
                                    .append(varname)
                                    .append(";\n");
                        }
                    }else{
                        if(isInterface) {
                            builder.append("this.")
                                    .append(varname)
                                    .append(" = ")
                                    .append(varname)
                                    .append(";\n");
                        }else{
                            builder.append("super.")
                                    .append(exElement.getSimpleName().toString())
                                    .append('(')
                                    .append(fName)
                                    .append(");\n");
                        }
                        builder.append("\n\t\tif(")
                                .append(fName)
                                .append("Observers != null){\n")
                                .append("\t\t\tfor(")
                                .append(MODEL_METHOD_INTERFACE_CLASS)
                                .append(" observer : ")
                                .append(fName)
                                .append("Observers){\n\t\t\t\tobserver.invoke(\"")
                                .append(fName)
                                .append("\", ")
                                .append(varname)
                                .append(");\n")
                                .append("\t\t\t}\n\t\t}\n");
                    }
                    builder.append("\t}\n\n");
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

            StringBuilder getFieldSwitch = new StringBuilder("\t\tswitch(field){\n");
            StringBuilder fieldGetters = new StringBuilder();
            StringBuilder fieldInstantiations = new StringBuilder();

            for(Element element : elements){
                Name fieldName = element.getSimpleName();
                element.getModifiers().contains(Modifier.PRIVATE);
                TypeMirror fieldType = element.asType();
                String fieldTypeName = fieldType.toString();

                fieldGetters.append("\tpublic ")
                        .append(fieldType.toString())
                        .append(" get")
                        .append(fieldName)
                        .append("(){\n\t\treturn scope.")
                        .append(fieldName)
                        .append(";\n\t}\n\n");


                String modelpackageName = getPackageName((TypeElement) typeUtils.asElement(fieldType));
                int periodIndex = fieldTypeName.lastIndexOf('.');

                fieldInstantiations.append("\n\t\tthis.scope.")
                        .append(fieldName)
                        .append(" = new ")
                        .append(modelpackageName)
                        .append(".")
                        .append(fieldTypeName.substring(periodIndex + 1))
                        .append("$$NgModel();");

                getFieldSwitch.append("\t\t\tcase \"")
                        .append(fieldName)
                        .append("\":\n\t\t\t\treturn (")
                        .append(MODEL_INTERFACE_CLASS)
                        .append(") scope.")
                        .append(fieldName)
                        .append(";\n");
            }
            getFieldSwitch.append("\t\t}\n\t\tthrow new com.ngandroid.lib.exceptions.NgException(\"Field '\" + field + \"' was not found in \" + getClass().getSimpleName());\n");

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
                        .append("> implements ")
                        .append(SCOPE_INTERFACE_CLASS)
                        .append(" {\n\n\tprivate T scope;\n")
                        .append("\tpublic ")
                        .append(scopeName)
                        .append("(T scope){\n\t\tthis.scope = scope;")
                        .append(fieldInstantiations)
                        .append("\n\t}\n\n")
                        .append("\tpublic ")
                        .append(MODEL_INTERFACE_CLASS)
                        .append(" getModel(String field){\n")
                        .append(getFieldSwitch)
                        .append("\t}\n\n")
                        .append(fieldGetters)
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