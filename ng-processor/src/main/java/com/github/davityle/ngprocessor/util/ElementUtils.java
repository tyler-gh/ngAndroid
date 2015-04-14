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

package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by tyler on 3/30/15.
 */
public class ElementUtils {

    private static Elements elementUtils;

    public static void setElements(Elements elements){
        elementUtils = elements;
    }

    public static boolean methodsMatch(Element elem, MethodSource source){
        if(elem == null || !(elem instanceof ExecutableElement) || elem.getKind() != ElementKind.METHOD)
            return false;
        ExecutableElement method = (ExecutableElement) elem;

        List<Source> parameters = source.getParameters();
        List<? extends VariableElement> methodParameters = method.getParameters();
        if(methodParameters.size() != parameters.size())
            return false;

        for(int i = 0; i < parameters.size(); i++){
            TypeMirror typeMirror = parameters.get(i).getTypeMirror();
            // TODO get the model types from the models in the matching scope and see if they match by type
            if(typeMirror != null){
                VariableElement element = methodParameters.get(i);
                TypeMirror eleType = element.asType();
                if (!TypeUtils.matchFirstPrecedence(eleType, typeMirror))
                    return false;
            }
        }

        return elem.getSimpleName().toString().equals(source.getMethodName()) && ElementUtils.isAccessible(elem);
    }

    public static boolean isSetter(Element elem){
        return elem != null && ExecutableElement.class.isInstance(elem)
                && elem.getKind() == ElementKind.METHOD
                && elem.getSimpleName().toString().startsWith("set")
                && ((ExecutableElement) elem).getParameters().size() == 1;
    }

    public static boolean isGetterForField(Element elem, String field, TypeKind typeKind){
        return elem != null && ExecutableElement.class.isInstance(elem)
                && elem.getKind() == ElementKind.METHOD
                && elem.getSimpleName().toString().toLowerCase().equals("get" + field)
                && ((ExecutableElement) elem).getReturnType().getKind() == typeKind
                && ((ExecutableElement) elem).getParameters().size() == 0;
    }

    public static boolean hasGetterAndSetter(TypeElement model, String field){
        boolean hasGetter = false, hasSetter = false;
        for(Element f : model.getEnclosedElements()){
            if(f.getSimpleName().toString().toLowerCase().equals("set" + field)){
                hasSetter = true;
                if(hasGetter)
                    break;
            }else if(f.getSimpleName().toString().toLowerCase().equals("get" + field)){
                hasGetter = true;
                if(hasSetter)
                    break;
            }
        }
        return hasGetter && hasSetter;
    }

    public static boolean returnsVoid(ExecutableElement method){
        TypeMirror type = method.getReturnType();
        return (type != null && type.getKind() == TypeKind.VOID);
    }

    public static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    public static boolean hasAnnotationWithName(Element element, String simpleName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String annotationName = mirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    public static String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    public static boolean isAccessible(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return !(modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED));
    }
}
