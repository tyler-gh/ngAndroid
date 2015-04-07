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

package com.github.davityle.ngprocessor.util.xml;

import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

/**
 * Created by tyler on 4/7/15.
 */
public class XmlFileData {

    private final File file;
    private final List<XmlNode> views;
    private final Map<XmlNode, List<Element>> viewScopeMap = new HashMap<>();
    private final Map<Element, List<XmlNode>> elementNodeMap = new HashMap<>();
    private boolean isMapped;

    public XmlFileData(File file, List<XmlNode> views) {
        this.file = file;
        this.views = views;
    }

    public Map<XmlNode, List<Element>> getViewScopeMap(List<Element> scopes){
        if(!isMapped){
            mapToScopes(scopes);
        }
        return viewScopeMap;
    }

    public Map<Element, List<XmlNode>> getElementNodeMap(List<Element> scopes){
        if(!isMapped){
            mapToScopes(scopes);
        }
        return elementNodeMap;
    }

    private void mapToScopes(List<Element> scopes){
        for (XmlNode view : views) {
            List<Element> matchingScopes = new ArrayList<>(scopes);
            Iterator<Element> it = matchingScopes.listIterator();
            for (;it.hasNext();) {
                Element scope = it.next();
                boolean match = true;
                for (XmlAttribute attribute : view.getAttributes()) {
                    List<MethodSource> methodSources = attribute.getMethodSource();
                    for (MethodSource methodSource : methodSources) {
                        boolean found = false;
                        for (Element child : scope.getEnclosedElements()) {
                            if (child.getSimpleName().toString().equals(methodSource.getMethodName())) {
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
                    List<ModelSource> modelSources = attribute.getModelSource();
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
                }
                if (!match) {
                    it.remove();
                }
            }
            put(view, matchingScopes);
        }
        isMapped = true;
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


    @Override
    public int hashCode() {
        return file.hashCode();
    }
}
