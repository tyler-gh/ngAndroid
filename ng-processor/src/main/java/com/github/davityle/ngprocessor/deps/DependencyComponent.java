package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.NgProcessor;
import com.github.davityle.ngprocessor.map.LayoutScopeMapper;
import com.github.davityle.ngprocessor.map.ModelScopeMapper;
import com.github.davityle.ngprocessor.source.SourceCreator;
import com.github.davityle.ngprocessor.source.linkers.ModelSourceLinker;
import com.github.davityle.ngprocessor.source.linkers.ScopeSourceLinker;
import com.github.davityle.ngprocessor.util.CollectionUtils;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.ManifestPackageUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.ScopeUtils;
import com.github.davityle.ngprocessor.xml.XmlUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    NgProcessor.EnvironmentModule.class,
    LayoutModule.class,
    AttrModule.class,
    UtilsModule.class
})
public interface DependencyComponent {
    ManifestPackageUtils createManifestPackageUtils();
    ScopeUtils createScopeUtils();
    MessageUtils createMessageUtils();
    XmlUtils createXmlUtils();
    CollectionUtils createCollectionUtils();
    ElementUtils elementUtils();

    void inject(ModelScopeMapper modelScopeMapper);
    void inject(LayoutScopeMapper layoutScopeMapper);
    void inject(ScopeSourceLinker scopeSourceLinker);
    void inject(ModelSourceLinker sourceLinker);
    void inject(SourceCreator sourceCreator);
}
