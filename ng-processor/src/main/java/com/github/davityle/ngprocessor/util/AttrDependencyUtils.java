package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.attributes.AttrDependencies;
import com.github.davityle.ngprocessor.attributes.AttrDependency;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by tyler on 7/6/15.
 */
public class AttrDependencyUtils {

    private final AttrDependencies attrDependencies;

    @Inject
    public AttrDependencyUtils(AttrDependencies attrDependencies) {
        this.attrDependencies = attrDependencies;
    }

    public Set<AttrDependency> getDependencies(Collection<String> attrs){
        Set<AttrDependency> deps = new HashSet<>();
        getDeps(attrs, deps);
        return deps;
    }

    private void getDeps(Collection<String> attrs, Set<AttrDependency> deps){
        for(String attr : attrs) {
            AttrDependency attrDependency = attrDependencies.get(attr);
            deps.add(attrDependency);
            getDeps(Arrays.asList(attrDependency.getDependencies()), deps);
        }
    }
}
