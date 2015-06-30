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
import com.github.davityle.ngprocessor.util.DefaultLayoutDirProvider;
import com.github.davityle.ngprocessor.util.LayoutScopeMapper;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.ModelScopeMapper;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.source.ModelSourceLinker;
import com.github.davityle.ngprocessor.util.source.ScopeSourceLinker;
import com.github.davityle.ngprocessor.util.source.SourceCreator;
import com.github.davityle.ngprocessor.util.xml.ManifestPackageUtils;
import com.github.davityle.ngprocessor.util.xml.XmlNode;

import java.io.File;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import dagger.Module;
import dagger.Provides;

@SupportedAnnotationTypes({
    ModelScopeMapper.NG_MODEL_ANNOTATION,
    NgScopeAnnotationUtils.NG_SCOPE_ANNOTATION
})
public class NgProcessor extends AbstractProcessor {

    private final LayoutModule layoutModule;
    private ProcessingEnvironment env;

    public NgProcessor(){
        this(Option.<String>absent());
    }

    public NgProcessor(final Option<String> option){
        this(new LayoutModule(new DefaultLayoutDirProvider() {
            @Override
            public Option<String> getDefaultLayoutDir() {
                return option;
            }
        }));
    }

    public NgProcessor(LayoutModule layoutModule){
        this.layoutModule = layoutModule;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.env = env;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if(annotations.size() == 0)
            return false;

        final DependencyComponent dependencyComponent = DaggerDependencyComponent
                .builder()
                .layoutModule(layoutModule)
                .environmentModule(new EnvironmentModule(roundEnv))
                .build();

        MessageUtils messageUtils = dependencyComponent.createMessageUtils();

        ManifestPackageUtils manifestPackageUtils = dependencyComponent.createManifestPackageUtils();

        messageUtils.note(null, ":NgAndroid:processing");

        String manifestPackageName = manifestPackageUtils.getPackageName();

        if(manifestPackageName == null) {
            messageUtils.error(null, ":NgAndroid:Unable to find android manifest.");
            return false;
        }

        // get the elements annotated with NgScope
        List<Element> scopes = dependencyComponent.createNgScopeAnnotationUtils().getScopes(annotations);

        // get the xml layouts/nodes with attributes
        Map<File, List<XmlNode>> fileNodeMap = dependencyComponent.createXmlUtils().getFileNodeMap();

        if(messageUtils.hasErrors())
            return false;

        LayoutScopeMapper layoutScopeMapper = new LayoutScopeMapper(scopes, fileNodeMap);
        ModelScopeMapper modelScopeMapper = new ModelScopeMapper(annotations, scopes);

        dependencyComponent.inject(layoutScopeMapper);
        dependencyComponent.inject(modelScopeMapper);

        // get the mapped models
        Map<String, Element> modelMap = modelScopeMapper.getModels();
        // get the mapped scopes
        Map<String, List<Element>> scopeMap = modelScopeMapper.getScopeMap();

        ModelSourceLinker sourceLinker = new ModelSourceLinker(modelMap);
        dependencyComponent.inject(sourceLinker);
        // get the model to source links
        List<NgModelSourceLink> modelSourceLinks = sourceLinker.getSourceLinks();
        // get the scope to source links
        ScopeSourceLinker scopeSourceLinker = new ScopeSourceLinker(scopes, scopeMap, layoutScopeMapper.getElementNodeMap(), manifestPackageName);
        dependencyComponent.inject(scopeSourceLinker);
        List<NgScopeSourceLink> scopeSourceLinks = scopeSourceLinker.getSourceLinks();

        // create the source files
        SourceCreator sourceCreator = new SourceCreator(modelSourceLinks, scopeSourceLinks);
        dependencyComponent.inject(sourceCreator);
        sourceCreator.createSourceFiles();

        if(!messageUtils.hasErrors()) {
            messageUtils.note(null, ":NgAndroid:successful");
            return true;
        } else {
            messageUtils.note(null, ":NgAndroid:failed");
            return false;
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Module
    public class EnvironmentModule {
        private final RoundEnvironment roundEnv;

        public EnvironmentModule(RoundEnvironment roundEnv){
            this.roundEnv = roundEnv;
        }

        @Provides
        public ProcessingEnvironment getProcessingEnvironment() {
            return processingEnv;
        }

        @Provides
        public Filer getFiler(){
            return env.getFiler();
        }

        @Provides
        public Types getTypeUtils(){
            return env.getTypeUtils();
        }

        @Provides
        public Elements getElementUtils(){
            return env.getElementUtils();
        }

        @Provides
        public RoundEnvironment getRoundEnv(){
            return roundEnv;
        }
    }
}