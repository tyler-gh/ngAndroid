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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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
}
