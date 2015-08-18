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

import com.github.davityle.ngprocessor.deps.DaggerDependencyComponent;
import com.github.davityle.ngprocessor.deps.DependencyComponent;
import com.github.davityle.ngprocessor.deps.LayoutModule;
import com.github.davityle.ngprocessor.finders.DefaultLayoutDirProvider;
import com.github.davityle.ngprocessor.map.LayoutScopeMapper;
import com.github.davityle.ngprocessor.model.Layout;
import com.github.davityle.ngprocessor.model.Scope;
import com.github.davityle.ngprocessor.source.SourceCreator;
import com.github.davityle.ngprocessor.source.linkers.ModelSourceLinker;
import com.github.davityle.ngprocessor.source.links.LayoutSourceLink;
import com.github.davityle.ngprocessor.source.links.NgModelSourceLink;
import com.github.davityle.ngprocessor.source.links.ScopeSourceLink;
import com.github.davityle.ngprocessor.util.CollectionUtils;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.ManifestPackageUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.ScopeUtils;
import com.github.davityle.ngprocessor.util.Tuple;
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
    ScopeUtils.NG_MODEL_ANNOTATION,
    ScopeUtils.NG_SCOPE_ANNOTATION
})
public class NgProcessor extends AbstractProcessor {

    private final DaggerDependencyComponent.Builder dependencyComponentBuilder;
    private final Option<EnvironmentResolver> envModule;
    private DependencyComponent dependencyComponent;
    private ProcessingEnvironment env;
    private RoundEnvironment roundEnv;

    public NgProcessor(){
        this(Option.<String>absent());
    }

    public NgProcessor(final Option<String> option){
        this(DaggerDependencyComponent.builder().layoutModule(new LayoutModule(new DefaultLayoutDirProvider() {
            @Override
            public Option<String> getDefaultLayoutDir() {
                return option;
            }
        })), Option.<EnvironmentResolver>absent());
    }

    public NgProcessor(DaggerDependencyComponent.Builder dependencyComponentBuilder, Option<EnvironmentResolver> envModule){
        this.dependencyComponentBuilder = dependencyComponentBuilder;
        this.envModule = envModule;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.env = env;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.roundEnv = roundEnv;
        if (annotations.size() == 0)
            return false;

        try {
            this.dependencyComponent = dependencyComponentBuilder
                    .environmentModule(new EnvironmentModule(envModule.getOrElse(new ResolverImpl())))
                    .build();
            final MessageUtils messageUtils = dependencyComponent.createMessageUtils();

            messageUtils.note(Option.<Element>absent(), ":NgAndroid:processing");

            Option<String> manifestPackageName = getPackageNameFromAndroidManifest();

            if (manifestPackageName.isAbsent()) {
                messageUtils.error(Option.<Element>absent(), ":NgAndroid:Unable to find android manifest.");
                return false;
            }

            Set<Scope> scopes = getScopeSet(annotations);
            Map<Layout, Collection<XmlScope>> xmlScopes = getXmlScopes();

            if (messageUtils.hasErrors())
                return false;

            Map<Layout, Collection<Scope>> layoutsWScopes = mapLayoutsToScopes(scopes, xmlScopes);
            Collection<LayoutSourceLink> layoutSourceLinks = getLayoutSourceLinks(layoutsWScopes, manifestPackageName.get());

            Collection<ScopeSourceLink> scopeSourceLinks = getScopeSourceLinks(scopes, manifestPackageName.get());
            List<NgModelSourceLink> modelSourceLinks = getModelSourceLinks(getModels(annotations));

            createSourceFiles(modelSourceLinks, layoutSourceLinks, scopeSourceLinks);

            messageUtils.note(Option.<Element>absent(), ":NgAndroid:finished");
            return true;
        } catch (Throwable t) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "There was an error compiling your ngAttributes: " + sw.toString().replace('\n', ' '), null);
            return false;
        }
    }

    private Map<Layout, Collection<Scope>> mapLayoutsToScopes(Set<Scope> scopes, Map<Layout, Collection<XmlScope>> xmlScopes){
        LayoutScopeMapper layoutScopeMapper = new LayoutScopeMapper(scopes, xmlScopes);
        dependencyComponent.inject(layoutScopeMapper);
        return layoutScopeMapper.mapLayoutsToScopes();
    }

    private Collection<Element> getModels(Set<? extends TypeElement> annotations) {
        return dependencyComponent.createScopeUtils().getModels(annotations);
    }

    private List<NgModelSourceLink>  getModelSourceLinks(Collection<Element> modelMap) {
        ModelSourceLinker sourceLinker = new ModelSourceLinker(modelMap);
        dependencyComponent.inject(sourceLinker);
        return sourceLinker.getSourceLinks();
    }

    private Collection<ScopeSourceLink> getScopeSourceLinks(final Set<Scope> scopes, final String packageName){
        return dependencyComponent.createCollectionUtils().map(scopes, new CollectionUtils.Function<Scope, ScopeSourceLink>() {
            @Override
            public ScopeSourceLink apply(Scope scope) {
                ElementUtils el = dependencyComponent.elementUtils();
                return new ScopeSourceLink(scope, el.getFullName(scope.getJavaElement()), packageName);
            }
        });
    }

    private Collection<LayoutSourceLink> getLayoutSourceLinks(final Map<Layout, Collection<Scope>> scopeMap, final String packageName){
        return dependencyComponent.createCollectionUtils().mapToCollection(scopeMap, new CollectionUtils.Function<Tuple<Layout, Collection<Scope>>, LayoutSourceLink>() {
            @Override
            public LayoutSourceLink apply(Tuple<Layout, Collection<Scope>> layout) {
                return new LayoutSourceLink(layout.getSecond(), layout.getFirst(), packageName);
            }
        });
    }

    private void createSourceFiles(List<NgModelSourceLink> modelSourceLinks, Collection<LayoutSourceLink> layoutSourceLinks, Collection<ScopeSourceLink> scopeSourceLinks) {
        SourceCreator sourceCreator = new SourceCreator(modelSourceLinks, layoutSourceLinks, scopeSourceLinks);
        dependencyComponent.inject(sourceCreator);
        sourceCreator.createSourceFiles();
    }


    private Option<String> getPackageNameFromAndroidManifest() {
        ManifestPackageUtils manifestPackageUtils = dependencyComponent.createManifestPackageUtils();
        return manifestPackageUtils.getPackageName();
    }

    private Set<Scope> getScopeSet(Set<? extends TypeElement> annotations) {
        return dependencyComponent.createScopeUtils().getScopes(annotations);
    }

    private Map<Layout, Collection<XmlScope>> getXmlScopes() {
       return dependencyComponent.createXmlUtils().getXmlScopes();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Module
    public static class EnvironmentModule {

        private final EnvironmentResolver resolver;

        public EnvironmentModule(EnvironmentResolver resolver) {
            this.resolver = resolver;
        }

        @Provides
        public ProcessingEnvironment getProcessingEnvironment() {
            return resolver.getProcessingEnvironment();
        }

        @Provides
        public Filer getFiler(){
            return resolver.getFiler();
        }

        @Provides
        public Types getTypeUtils(){
            return resolver.getTypeUtils();
        }

        @Provides
        public Elements getElementUtils(){
            return resolver.getElementUtils();
        }

        @Provides
        public RoundEnvironment getRoundEnv(){
            return resolver.getRoundEnv();
        }
    }

    public class ResolverImpl implements EnvironmentResolver{
        public ProcessingEnvironment getProcessingEnvironment() {
            return processingEnv;
        }

        public Filer getFiler(){
            return env.getFiler();
        }

        public Types getTypeUtils(){
            return env.getTypeUtils();
        }

        public Elements getElementUtils(){
            return env.getElementUtils();
        }

        public RoundEnvironment getRoundEnv(){
            return roundEnv;
        }
    }

    public interface EnvironmentResolver {
        ProcessingEnvironment getProcessingEnvironment();
        Filer getFiler();
        Types getTypeUtils();
        Elements getElementUtils();
        RoundEnvironment getRoundEnv();
    }
}