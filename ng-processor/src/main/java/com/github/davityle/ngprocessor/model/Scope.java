package com.github.davityle.ngprocessor.model;

import com.github.davityle.ngprocessor.xml.XmlScope;
import com.github.davityle.ngprocessor.xml.XmlView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

public class Scope {

    private final Element javaElement;
    private final Collection<Model> ngModels;
    private final String scopeName, scopeType, javaName;
    private final Map<Layout, XmlScope> xmlScopes;

    public Scope(Element javaElement, Collection<Model> ngModels, String scopeName, String scopeType, String javaName) {
        this.javaElement = javaElement;
        this.ngModels = ngModels;
        this.scopeName = scopeName;
        this.scopeType = scopeType;
        this.javaName = javaName;
        this.xmlScopes = new HashMap<>();
    }

    public void addXmlScope(Layout layout, XmlScope xmlScope) {
        xmlScopes.put(layout, xmlScope);
    }

    public Map<Layout, XmlScope> getXmlScopes() {
        return xmlScopes;
    }

    public boolean inLayout(String layout) {
        return xmlScopes.containsKey(new Layout(layout));
    }

    public Collection<Layout> getLayouts() {
        return xmlScopes.keySet();
    }

    public Collection<XmlView> getViews(String layout) {
        return xmlScopes.get(new Layout(layout)).getViews();
    }

    public Element getJavaElement() {
        return javaElement;
    }

    public Collection<Model> getNgModels() {
        return ngModels;
    }

    public String getName() {
        return scopeName;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getTypeName() {
        return scopeType;
    }

    @Override
    public String toString() {
        return javaElement.toString();
    }

    @Override
    public int hashCode() {
        return javaElement.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return javaElement.equals(obj);
    }
}
