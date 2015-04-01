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

import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.sourcelinks.NgModelSourceLink;
import com.github.davityle.ngprocessor.sourcelinks.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.NgModelAnnotationUtils;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;
import com.github.davityle.ngprocessor.util.source.NgModelSourceUtils;
import com.github.davityle.ngprocessor.util.source.NgScopeSourceUtils;
import com.github.davityle.ngprocessor.util.source.SourceCreator;
import com.github.davityle.ngprocessor.util.xml.XmlAttribute;
import com.github.davityle.ngprocessor.util.xml.XmlNode;
import com.github.davityle.ngprocessor.util.xml.XmlUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * this class is currently experimental.
 * Looking at it too closely might make your eyes bleed....
 *
 * Parse xml
 * valid syntax of attributes
 * map models to scopes
 * check to see if each xml file containing ngattributes matches at least 1 scope
 *
 */

@SupportedAnnotationTypes({
    NgModelAnnotationUtils.NG_MODEL_ANNOTATION,
    NgScopeAnnotationUtils.NG_SCOPE_ANNOTATION
})
public class NgProcessor extends AbstractProcessor {

    private Filer filer;

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        ElementUtils.setElements(env.getElementUtils());
        MessageUtils.setProcessingEnv(processingEnv);
        TypeUtils.setTypeUtils(env.getTypeUtils());
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.size() == 0)
            return false;
        System.out.println(":NgAndroid:processing");

        Map<File, List<XmlNode>> xmlAttrMap = XmlUtils.getAttributes();

        List<Element> scopes = NgScopeAnnotationUtils.getScopes(annotations, roundEnv);
        Map<String, List<Element>> scopeBuilderMap = NgScopeAnnotationUtils.getScopeMap(scopes);
        Map<String, Element> modelBuilderMap = NgModelAnnotationUtils.getModels(annotations, roundEnv, scopeBuilderMap);

        List<NgModelSourceLink> modelSourceLinks = NgModelSourceUtils.getSourceLinks(modelBuilderMap);
        List<NgScopeSourceLink> scopeSourceLinks = NgScopeSourceUtils.getSourceLinks(scopeBuilderMap);

        Set<Map.Entry<File, List<XmlNode>>> xmlLayouts = xmlAttrMap.entrySet();
        Map<XmlNode, List<Element>> viewScopeMap = new HashMap<>();

        for (Map.Entry<File, List<XmlNode>> layout : xmlLayouts) {
            List<XmlNode> views = layout.getValue();

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
                viewScopeMap.put(view, matchingScopes);
            }
        }

        Set<Map.Entry<XmlNode, List<Element>>> entries = viewScopeMap.entrySet();
        for(Map.Entry<XmlNode, List<Element>> entry : entries){
            if(entry.getValue().size() == 0){
                MessageUtils.error(null, "This view does not match any scope specifically this element did not match");
            }
        }


        SourceCreator sourceCreator = new SourceCreator(filer, modelSourceLinks, scopeSourceLinks);
        sourceCreator.createSourceFiles();

        System.out.println(":NgAndroid:successful");
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}