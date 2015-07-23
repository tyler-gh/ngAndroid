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

import com.github.davityle.ngprocessor.attributes.AttrDependency;
import com.github.davityle.ngprocessor.deps.AttrModule;
import com.github.davityle.ngprocessor.deps.DaggerDependencyComponent;
import com.github.davityle.ngprocessor.deps.DependencyComponent;
import com.github.davityle.ngprocessor.deps.LayoutModule;
import com.github.davityle.ngprocessor.finders.DefaultLayoutDirProvider;
import com.github.davityle.ngprocessor.map.LayoutScopeMapper;
import com.github.davityle.ngprocessor.map.ModelScopeMapper;
import com.github.davityle.ngprocessor.source.SourceCreator;
import com.github.davityle.ngprocessor.source.linkers.ModelSourceLinker;
import com.github.davityle.ngprocessor.source.linkers.ScopeSourceLinker;
import com.github.davityle.ngprocessor.source.links.NgModelSourceLink;
import com.github.davityle.ngprocessor.source.links.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.AttrDependencyUtils;
import com.github.davityle.ngprocessor.util.ManifestPackageUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.XmlNodeUtils;
import com.github.davityle.ngprocessor.xml.XmlScope;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
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
import javax.tools.Diagnostic;

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
        try {
            if (annotations.size() == 0)
                return false;

            final DependencyComponent dependencyComponent = DaggerDependencyComponent
                    .builder()
                    .layoutModule(layoutModule)
                    .environmentModule(new EnvironmentModule(roundEnv))
                    .attrModule(new AttrModule())
                    .build();

            MessageUtils messageUtils = dependencyComponent.createMessageUtils();
            messageUtils.note(null, ":NgAndroid:processing");

            Option<String> manifestPackageName = getPackageNameFromAndroidManifest(dependencyComponent);

            if (manifestPackageName.isAbsent()) {
                messageUtils.error(null, ":NgAndroid:Unable to find android manifest.");
                return false;
            }

            // get the elements annotated with NgScope
            List<Element> scopes = dependencyComponent.createNgScopeAnnotationUtils().getScopes(annotations);

            // get the xml layouts/nodes with attributes
            Map<String, Collection<XmlScope>> fileNodeMap = dependencyComponent.createXmlUtils().getFileNodeMap();

            if (messageUtils.hasErrors())
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
            ScopeSourceLinker scopeSourceLinker = new ScopeSourceLinker(scopes, scopeMap, layoutScopeMapper.getElementNodeMap(), manifestPackageName.get());
            dependencyComponent.inject(scopeSourceLinker);
            List<NgScopeSourceLink> scopeSourceLinks = scopeSourceLinker.getSourceLinks();

            AttrDependencyUtils attrDependencyUtils = dependencyComponent.createAttrDependencyUtils();
            XmlNodeUtils xmlNodeUtils = dependencyComponent.createXmlNodeUtils();

            Set<AttrDependency> dependencySet = attrDependencyUtils.getDependencies(xmlNodeUtils.getAttributes(fileNodeMap));

            // create the source files
            SourceCreator sourceCreator = new SourceCreator(modelSourceLinks, scopeSourceLinks, dependencySet);
            dependencyComponent.inject(sourceCreator);
            sourceCreator.createSourceFiles();

            messageUtils.note(null, ":NgAndroid:finished");
            return true;
        } catch (Throwable t) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "There was an error compiling your ngAttributes: " + sw.toString().replace('\n', ' '), null);
            return false;
        }
    }

    private Option<String> getPackageNameFromAndroidManifest(DependencyComponent dependencyComponent) {
        ManifestPackageUtils manifestPackageUtils = dependencyComponent.createManifestPackageUtils();
        return manifestPackageUtils.getPackageName();
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