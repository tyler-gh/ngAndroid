package com.github.davityle.ngprocessor.attributes;

import javax.inject.Inject;

/**
 * Created by tyler on 7/3/15.
 */
public class AttrDependencies extends AttrDependencyMap {
    private final AttrPackageResolver attrPackageResolver;

    @Inject
    public AttrDependencies(Attributes attributes, AttrPackageResolver attrPackageResolver){
        
        putAll(attributes);
        this.attrPackageResolver = attrPackageResolver;

        put(new AttrDependency("CompoundButtonInteractor"));
        put(new AttrDependency("Executor"));
        put(new AttrDependency("FireCheckObserver"));
        put(new AttrDependency("SetTextModelObserver"));
        put(new AttrDependency("TextInteracter"));
    }

    public String getAttributesPackage(){
        return attrPackageResolver.getPackage();
    }
}


