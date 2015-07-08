package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.attributes.AttrPackageResolver;

import dagger.Module;
import dagger.Provides;

@Module
public class AttrModule {
    @Provides
    public AttrPackageResolver attrPackageResolver(){
        return new AttrPackageResolver() {
            @Override
            public String getPackage() {
                return "com.ngandroid.lib.ngattributes";
            }

            @Override
            public String getAttrClassName() {
                return "AttrsImpl";
            }
        };
    }
}
