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
import com.github.davityle.ngprocessor.util.xml.XmlAttribute;
import com.github.davityle.ngprocessor.util.xml.XmlNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 4/7/15.
 */
public class LayoutScopeMapper {

    private final Set<Map.Entry<File, List<XmlNode>>> xmlLayouts;
    private final Map<XmlNode, List<Element>> viewScopeMap = new HashMap<>();
    private final Map<Element, List<XmlNode>> elementNodeMap = new HashMap<>();
    private final List<Element> scopes;
    private boolean isMapped;

    public LayoutScopeMapper(List<Element> scopes, Map<File, List<XmlNode>> fileNodeMap){
        this.scopes = scopes;
        this.xmlLayouts = fileNodeMap.entrySet();
    }

    public Map<XmlNode, List<Element>> getViewScopeMap(){
        if(!isMapped){
            linkLayouts();
        }
        return viewScopeMap;
    }

    public Map<Element, List<XmlNode>> getElementNodeMap(){
        if(!isMapped){
            linkLayouts();
        }
        return elementNodeMap;
    }

    public void checkLayoutsValid(){
        Set<Map.Entry<XmlNode, List<Element>>> entries = getViewScopeMap().entrySet();
        for(Map.Entry<XmlNode, List<Element>> entry : entries){
            if(entry.getValue().size() == 0){
                MessageUtils.error(null, "This view does not match any scope specifically this element did not match");
            }
        }
    }

    private void linkLayouts(){
        for (Map.Entry<File, List<XmlNode>> layout : xmlLayouts) {
            List<XmlNode> views = layout.getValue();

            for (XmlNode view : views) {
                List<Element> matchingScopes = new ArrayList<>(scopes);
                for (Iterator<Element> it = matchingScopes.listIterator(); it.hasNext();) {
                    Element scope = it.next();
                    if (!scopeMatchesXmlView(scope, view)) {
                        it.remove();
                    }
                }
                put(view, matchingScopes);
            }
        }
        isMapped = true;
    }

    private boolean scopeHasMethods(Element scope, List<MethodSource> methodSources, Map<ModelSource,ModelSource> typedModels){
        boolean match = true;
        for (MethodSource methodSource : methodSources) {
            boolean found = false;
            for (Element child : scope.getEnclosedElements()) {
                if(ElementUtils.methodsMatch(child, methodSource, typedModels)){
                    found = true;
                    break;
                }
            }
            if (!found) {
                // TODO list attribute as not found
                match = false;
                break;
            }
        }
        return match;
    }

    private boolean scopeHasModels(Element scope, List<ModelSource> modelSources){
        boolean match = true;
        for (ModelSource modelSource : modelSources) {
            boolean found = false;
            for (Element child : scope.getEnclosedElements()) {
                if (child.getSimpleName().toString().equals(modelSource.getModelName())) {
                    if(ElementUtils.hasGetterAndSetter(TypeUtils.asTypeElement(child.asType()), modelSource.getFieldName())) {
                        found = true;
                        break;
                    }else{
                        //TODO field was found but model did not have getter and setter
                    }
                }
            }
            if (!found) {
                // TODO list attribute as not found
                match = false;
                break;
            }
        }
        return match;
    }

    private boolean scopeMatchesXmlView(Element scope, XmlNode view){
        boolean match = true;
        for (XmlAttribute attribute : view.getAttributes()) {
            Map<ModelSource, ModelSource> typedModels = mapScopeToModels(scope, attribute.getModelSource());
            if(typedModels == null){
                match = false;
            }else{
//                match = scopeHasModels(scope, attribute.getModelSource());
                match = match && scopeHasMethods(scope, attribute.getMethodSource(), typedModels);
            }
        }
        return match;
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
                    if(ElementUtils.hasGetterAndSetter(typeElement, fieldName)) {
                        ModelSource copy = modelSource.copy(ElementUtils.getElementType(typeElement, fieldName));
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

    private void put(XmlNode view, List<Element> matchingScopes){
        viewScopeMap.put(view, matchingScopes);
        for(Element element : matchingScopes){
            List<XmlNode> nodes =  elementNodeMap.get(element);
            if(nodes == null){
                nodes = new ArrayList<>();
                elementNodeMap.put(element, nodes);
            }
            nodes.add(view);
        }
    }

}
