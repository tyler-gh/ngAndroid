package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.xml.XmlScope;
import com.github.davityle.ngprocessor.xml.XmlView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

public class Scope {

    private final Element javaElement;
    private final Collection<Model> ngModels;
    private final String scopeName, scopeType;
    private final Map<String, XmlScope> xmlScopes;

    public Scope(Element javaElement, Collection<Model> ngModels, String scopeName, String scopeType) {
        this.javaElement = javaElement;
        this.ngModels = ngModels;
        this.scopeName = scopeName;
        this.scopeType = scopeType;
        this.xmlScopes = new HashMap<>();
    }

    public void addXmlScope(String layout, XmlScope xmlScope) {
        xmlScopes.put(layout, xmlScope);
    }

    public Map<String, XmlScope> getXmlScopes() {
        return xmlScopes;
    }

    public boolean inLayout(String layout) {
        return xmlScopes.containsKey(layout);
    }

    public Collection<XmlView> getViews(String layout) {
        return xmlScopes.get(layout).getViews();
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
