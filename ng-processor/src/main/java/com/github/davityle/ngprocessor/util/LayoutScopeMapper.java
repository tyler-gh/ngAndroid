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
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.util.xml.TypedXmlAttribute;
import com.github.davityle.ngprocessor.util.xml.TypedXmlNode;
import com.github.davityle.ngprocessor.util.xml.XmlAttribute;
import com.github.davityle.ngprocessor.util.xml.XmlNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 4/7/15.
 */
public class LayoutScopeMapper {

    private final Set<Map.Entry<File, List<XmlNode>>> xmlLayouts;
    private final Map<Element, List<XmlNode>> elementNodeMap = new HashMap<>();
    private final List<Element> scopes;
    private boolean isMapped;

    public LayoutScopeMapper(List<Element> scopes, Map<File, List<XmlNode>> fileNodeMap){
        this.scopes = scopes;
        this.xmlLayouts = fileNodeMap.entrySet();
    }


    public Map<Element, List<XmlNode>> getElementNodeMap(){
        if(!isMapped){
            linkLayouts();
        }
        return elementNodeMap;
    }

    private void checkLayoutsValid(Map<XmlNode, Boolean> viewScopeMap){
        Set<Map.Entry<XmlNode, Boolean>> entries = viewScopeMap.entrySet();
        for(Map.Entry<XmlNode, Boolean> entry : entries){
            if(entry.getValue() == null || !entry.getValue()){
                MessageUtils.error(null, "This view does not match any scope specifically this element did not match");
            }
        }
    }

    private void linkLayouts(){
        final Map<XmlNode, Boolean> viewScopeMap = new HashMap<>();

        for (Map.Entry<File, List<XmlNode>> layout : xmlLayouts) {
            List<XmlNode> views = layout.getValue();
            for (XmlNode view : views) {
                for (Element scope : scopes) {
                    TypedXmlNode node = scopeMatchesXmlView(scope, view);
                    if (node != null) {
                        put(node, scope);
                        viewScopeMap.put(view, true);
                    }
                }
            }
        }

        checkLayoutsValid(viewScopeMap);
        isMapped = true;
    }

    private void put(XmlNode view, Element element){
            List<XmlNode> nodes =  elementNodeMap.get(element);
            if(nodes == null){
                nodes = new ArrayList<>();
                elementNodeMap.put(element, nodes);
            }
            nodes.add(view);
    }

    private TypedXmlNode scopeMatchesXmlView(Element scope, XmlNode view){
        boolean match = true;
        List<TypedXmlAttribute> typedAttributes = new ArrayList<>();
        for (XmlAttribute attribute : view.getAttributes()) {
            Map<ModelSource, ModelSource> typedModels = mapScopeToModels(scope, attribute.getModelSource());
            Map<MethodSource, MethodSource> typedMethods = mapScopeToMethods(scope, attribute.getMethodSource(), typedModels);
            match = match && typedMethods != null;
            if(match){
                typedAttributes.add(new TypedXmlAttribute(attribute, typedModels, typedMethods));
            }
        }
        return match ? new TypedXmlNode(view, typedAttributes) : null;
    }

    private Map<MethodSource, MethodSource> mapScopeToMethods(Element scope, List<MethodSource> methodSources, Map<ModelSource,ModelSource> typedModels){
        if(typedModels == null)
            return null;
        Map<MethodSource, MethodSource> methods = new HashMap<>();
        for (MethodSource methodSource : methodSources) {
            boolean found = false;
            for (Element child : scope.getEnclosedElements()) {
                if(ElementUtils.methodsMatch(child, methodSource, typedModels)){
                    MethodSource copy = methodSource.copy(((ExecutableElement) child).getReturnType());
                    methods.put(copy, copy);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // TODO list attribute as not found
                return null;
            }
        }
        return methods;
    }

    /**
     * maps the scope to the models
     * sets the types of the scope fields to the models
     * @param scope
     * @param modelSources
     * @return
     */
    private Map<ModelSource, ModelSource> mapScopeToModels(Element scope, List<ModelSource> modelSources){
        Map<ModelSource, ModelSource> mappedSources = new HashMap<>();
        for (ModelSource modelSource : modelSources) {
            boolean found = false;
            for (Element child : scope.getEnclosedElements()) {
                if (child.getSimpleName().toString().equals(modelSource.getModelName())) {
                    TypeMirror childType = child.asType();
                    TypeElement typeElement = TypeUtils.asTypeElement(childType);
                    String fieldName = modelSource.getFieldName();
                    Tuple<String, String> methods = ElementUtils.getGetAndSetMethodNames(typeElement, fieldName);
                    if(methods.getFirst() != null && methods.getSecond() != null) {
                        ModelSource copy = modelSource.copy(ElementUtils.getElementType(typeElement, fieldName));
                        copy.setGetter(methods.getFirst());
                        copy.setSetter(methods.getSecond());
                        mappedSources.put(copy, copy);
                        found = true;
                        break;
                    } //TODO field was found but model did not have getter and setter
                }
            }
            if (!found) // TODO list attribute as not found
                return null;
        }
        return mappedSources;
    }

}
