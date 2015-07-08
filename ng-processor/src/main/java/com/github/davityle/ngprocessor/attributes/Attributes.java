package com.github.davityle.ngprocessor.attributes;

import javax.inject.Inject;

public class Attributes extends AttributeMap {
    @Inject
    public Attributes() {
        put(new Attribute("NgBlur"));
        put(new Attribute("NgChange").setDependencies("Executor"));
        put(new Attribute("NgClick").setDependencies("Executor"));
        put(new Attribute("NgDisabled"));
        put(new Attribute("NgFocus"));
        put(new Attribute("NgGone").setDependencies("FireCheckObserver"));
        put(new Attribute("NgIf"));
        put(new Attribute("NgInvisible").setDependencies("FireCheckObserver"));
        put(new Attribute("NgLongClick"));
        put(new Attribute("NgModel").setAttrParameters("formatter").setDependencies("CompoundButtonInteractor", "TextInteracter"));
        put(new Attribute("NgText").setAttrParameters("formatter").setDependencies("SetTextModelObserver"));
    }
}


