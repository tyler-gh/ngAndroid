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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by tyler on 3/31/15.
 */
public class NgScopeAnnotationUtils {

    public static final String NG_SCOPE_ANNOTATION = "com.ngandroid.lib.annotations.NgScope";
    public static final String SCOPE_APPENDAGE = "$$NgScope";

    public static List<Element> getScopes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        List<Element> scopes = new ArrayList<Element>();
        for (TypeElement annotation : annotations) {
            if(NG_SCOPE_ANNOTATION.equals(annotation.getQualifiedName().toString())) {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element scopeClass : elements) {
                    scopes.add(scopeClass);
                }
            }
        }
        return scopes;
    }

    public static Map<String, List<Element>> getScopeMap(List<Element> scopes){
        Map<String, List<Element>> scopeBuilderMap = new LinkedHashMap<String, List<Element>>();
        for (Element scopeClass : scopes) {
            Set<Modifier> modifiers = scopeClass.getModifiers();
            if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                MessageUtils.error(scopeClass, "Unable to access Scope '%s'. Must have default or public access", scopeClass.toString());
                continue;
            }

            String packageName = ElementUtils.getPackageName((TypeElement) scopeClass);
            String className = ElementUtils.getClassName((TypeElement) scopeClass, packageName);
            String scopeName = className + NgScopeAnnotationUtils.SCOPE_APPENDAGE;
            String key = packageName + "." + scopeName;
            scopeBuilderMap.put(key,  new ArrayList<Element>());
        }
        return scopeBuilderMap;
    }

}
