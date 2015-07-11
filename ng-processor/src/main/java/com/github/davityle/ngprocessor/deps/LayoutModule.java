package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.finders.DefaultLayoutDirProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LayoutModule {

    private final DefaultLayoutDirProvider defaultLayoutDirProvider;

    public LayoutModule(DefaultLayoutDirProvider defaultLayoutDirProvider) {
        this.defaultLayoutDirProvider = defaultLayoutDirProvider;
    }


    @Provides
    public DefaultLayoutDirProvider getDefaultDirProvider(){
        return defaultLayoutDirProvider;
    }
}
