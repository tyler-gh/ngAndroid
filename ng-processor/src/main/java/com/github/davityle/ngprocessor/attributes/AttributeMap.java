package com.github.davityle.ngprocessor.attributes;

import java.util.HashMap;

class AttributeMap extends HashMap<String, Attribute> {
    public Attribute put(Attribute value) {
        super.put(value.getAttrName(), value);
        return super.put(value.getClassName(), value);
    }

    protected void put(AttrDependency attrDependency) {
        put((Attribute)attrDependency);
    }
}
