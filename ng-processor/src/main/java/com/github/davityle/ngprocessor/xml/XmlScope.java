package com.github.davityle.ngprocessor.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XmlScope {
    private final String scopeName;
    private final List<XmlAttribute> attributes = new ArrayList<>();

    public XmlScope(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeName() {
        return scopeName;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    public XmlScope addAttributes(Collection<XmlAttribute> attributes) {
        this.attributes.addAll(attributes);
        return this;
    }

    @Override
    public String toString() {
        return scopeName + ":" + attributes;
    }
}
