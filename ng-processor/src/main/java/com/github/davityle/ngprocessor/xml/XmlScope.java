package com.github.davityle.ngprocessor.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XmlScope {
    private final String scopeName;
    private final List<XmlView> views = new ArrayList<>();

    public XmlScope(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeName() {
        return scopeName;
    }

    public List<XmlView> getViews() {
        return views;
    }

    public XmlScope addViews(Collection<XmlView> views) {
        this.views.addAll(views);
        return this;
    }

    @Override
    public String toString() {
        return scopeName + ":" + views;
    }
}
