package com.github.davityle.ngprocessor.attributes;

import javax.inject.Inject;

public class Attributes extends AttributeMap {
    @Inject
    public Attributes(ScopeAttrNameResolver scopeAttrNameResolver) {
        put(new Attribute(scopeAttrNameResolver.getScopeAttrNameUppercase()));
        put(new Attribute("NgBlur").setAttrParameters("blur"));
        put(new Attribute("NgChange"));
        put(new Attribute("NgClick"));
        put(new Attribute("NgDisabled"));
        put(new Attribute("NgFocus"));
        put(new Attribute("NgGone"));
        put(new Attribute("NgInvisible"));
        put(new Attribute("NgLongClick"));
        put(new Attribute("NgModel").setAttrParameters("formatter"));
        put(new Attribute("NgText").setAttrParameters("formatter"));
    }
}


