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
import com.github.davityle.ngprocessor.util.xml.XmlNode;
import com.github.davityle.ngprocessor.util.xml.XmlUtils;

import java.io.File;
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

        Map<String, List<Element>> scopeBuilderMap = new LinkedHashMap<>();
        Map<String, Element> modelBuilderMap = NgModelAnnotationUtils.getModels(annotations, roundEnv, scopeBuilderMap);
        List<NgModelSourceLink> modelSourceLinks = NgModelSourceUtils.getSourceLinks(modelBuilderMap);
        List<NgScopeSourceLink> scopeSourceLinks = NgScopeSourceUtils.getSourceLinks(scopeBuilderMap);

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