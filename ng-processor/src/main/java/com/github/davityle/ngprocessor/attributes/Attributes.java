package com.github.davityle.ngprocessor.attributes;

import com.github.davityle.ngprocessor.util.Option;

import javax.inject.Inject;

public class Attributes extends AttributeMap {
    @Inject
    public Attributes(ScopeAttrNameResolver scopeAttrNameResolver) {
        put(new Attribute(scopeAttrNameResolver.getScopeAttrNameUppercase(), Option.<String>absent()));
        put(new Attribute("NgBlur").setAttrParameters("blur").setDependencies("NgIf"));
        put(new Attribute("NgChange").setDependencies("Executor"));
        put(new Attribute("NgClick").setDependencies("Executor"));
        put(new Attribute("NgDisabled").setDependencies("NgIf"));
        put(new Attribute("NgFocus").setDependencies("NgIf"));
        put(new Attribute("NgGone").setDependencies("FireCheckObserver", "NgIf"));
        put(new Attribute("NgInvisible").setDependencies("FireCheckObserver", "NgIf"));
        put(new Attribute("NgLongClick"));
        put(new Attribute("NgModel").setAttrParameters("formatter").setDependencies("CompoundButtonInteractor", "TextInteracter"));
        put(new Attribute("NgText").setAttrParameters("formatter").setDependencies("SetTextModelObserver"));
    }
}


