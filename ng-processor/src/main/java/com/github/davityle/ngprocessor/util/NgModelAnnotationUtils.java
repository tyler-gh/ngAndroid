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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 3/30/15.
 */
public class NgModelAnnotationUtils {

    public static final String NG_MODEL_ANNOTATION = "com.ngandroid.lib.annotations.NgModel";
    public static final String MODEL_APPENDAGE = "$$NgModel";

    public static Map<String, Element> getModels(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Map<String, List<Element>> scopeMap){

        Map<String, Element> modelBuilderMap = new LinkedHashMap<>();

        for (TypeElement annotation : annotations) {
            if(NG_MODEL_ANNOTATION.equals(annotation.getQualifiedName().toString())) {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element element : elements) {

                    Set<Modifier> modifiers = element.getModifiers();
                    if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                        MessageUtils.error(element, "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
                        continue;
                    }

                    TypeMirror fieldType = element.asType();
                    String fieldTypeName = fieldType.toString();
                    modelBuilderMap.put(fieldTypeName, element);

                    Element scopeClass = element.getEnclosingElement();
                    String packageName = ElementUtils.getPackageName((TypeElement) scopeClass);
                    String className = ElementUtils.getClassName((TypeElement) scopeClass, packageName);
                    String scopeName = className + NgScopeAnnotationUtils.SCOPE_APPENDAGE;
                    String key = packageName + "." + scopeName;
                    List<Element> els = scopeMap.get(key);
                    if (els == null) {
                        MessageUtils.error(scopeClass, "Missing NgScope annotation on Scope '%s'.", scopeClass.toString());
                    }else {
                        els.add(element);
                    }
                }
            }
        }

        return modelBuilderMap;
    }

}
