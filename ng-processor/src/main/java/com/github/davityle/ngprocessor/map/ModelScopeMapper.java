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

package com.github.davityle.ngprocessor.map;

import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.ScopeUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by tyler on 3/30/15.
 */
public class ModelScopeMapper {

//    public static final String NG_MODEL_ANNOTATION = "com.ngandroid.lib.annotations.NgModel";
    public static final String MODEL_APPENDAGE = "$$NgModel";

    private final Set<? extends TypeElement> annotations;
//    private final Set<Element> scopes;
    private final Map<String, Element> modelMap;

//    private Map<String, List<Element>> scopeMap;
    private boolean mapped;

    @Inject
    ElementUtils elementUtils;
    @Inject
    RoundEnvironment roundEnv;
    @Inject
    ScopeUtils scopeUtils;
    @Inject
    MessageUtils messageUtils;

    public ModelScopeMapper(Set<? extends TypeElement> annotations) {
        this.annotations = annotations;
        this.modelMap = new LinkedHashMap<>();
//        this.scopes = scopes;
    }

    public Map<String, Element> getModels(){
        if(!mapped){
            map();
        }
        return modelMap;
    }

//    public Map<String, List<Element>> getScopeMap(){
//        if(!mapped){
//            map();
//        }
//        return scopeMap;
//    }

    private void map(){
//        scopeMap = ngScopeAnnotationUtils.getScopeMap(scopes);

//        for (TypeElement annotation : annotations) {
//            if(NG_MODEL_ANNOTATION.equals(annotation.getQualifiedName().toString())) {
//                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
//                for (Element element : elements) {
//                    if (!elementUtils.isAccessible(element)) {
//                        messageUtils.error(Option.of(element), "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
//                        continue;
//                    }
//
//                    TypeMirror fieldType = element.asType();
//                    String fieldTypeName = fieldType.toString();
//                    modelMap.put(fieldTypeName, element);

//                    Element scopeClass = element.getEnclosingElement();
//                    String packageName = elementUtils.getPackageName((TypeElement) scopeClass);
//                    String className = elementUtils.getClassName((TypeElement) scopeClass, packageName);
//                    String scopeName = className + NgScopeAnnotationUtils.SCOPE_APPENDAGE;
//                    String key = packageName + "." + scopeName;
//                    List<Element> els = scopeMap.get(key);
//                    if (els == null) {
//                        messageUtils.error(Option.of(scopeClass), "Missing NgScope annotation on Scope '%s'.", scopeClass.toString());
//                    }else {
//                        els.add(element);
//                    }
//                }
//            }
//        }
//        checkDuplicates();
//        mapped = true;
    }

//    private void checkDuplicates(){
//        Collection<List<Element>> scopeModels = scopeMap.values();
//
//        for(List<Element> models : scopeModels){
//            Set<String> modelSet = new HashSet<>();
//            for(Element model : models){
//                String name = model.getSimpleName().toString().toLowerCase();
//                if(modelSet.contains(name)){
//                    messageUtils.error(Option.of(model), "Model '%s' has duplicate name. Names are checked by lowercase value.", name);
//                }else{
//                    modelSet.add(name);
//                }
//            }
//        }
//    }

}
