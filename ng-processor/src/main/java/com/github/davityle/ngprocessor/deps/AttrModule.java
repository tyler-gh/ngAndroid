package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.attributes.AttrPackageResolver;
import com.github.davityle.ngprocessor.attributes.ScopeAttrNameResolver;

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

    @Provides
    public ScopeAttrNameResolver scopeAttrNameResolver() {
        return new ScopeAttrNameResolver() {
            @Override
            public String getScopeAttrName() {
                return "ngScope";
            }

            @Override
            public String getScopeAttrNameUppercase() {
                return "NgScope";
            }
        };
    }
}
