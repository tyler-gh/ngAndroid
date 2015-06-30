package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.LayoutScopeMapper;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.ModelScopeMapper;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.source.ModelSourceLinker;
import com.github.davityle.ngprocessor.util.source.ScopeSourceLinker;
import com.github.davityle.ngprocessor.util.source.SourceCreator;
import com.github.davityle.ngprocessor.util.xml.ManifestPackageUtils;
import com.github.davityle.ngprocessor.util.xml.XmlUtils;

import dagger.Component;

@Component(modules = {
    NgProcessor.EnvironmentModule.class,
    LayoutModule.class
})
public interface DependencyComponent {
    ManifestPackageUtils createManifestPackageUtils();
    NgScopeAnnotationUtils createNgScopeAnnotationUtils();
    MessageUtils createMessageUtils();
    XmlUtils createXmlUtils();

    void inject(ModelScopeMapper modelScopeMapper);
    void inject(LayoutScopeMapper layoutScopeMapper);
    void inject(ScopeSourceLinker scopeSourceLinker);
    void inject(ModelSourceLinker sourceLinker);
    void inject(SourceCreator sourceCreator);
}
