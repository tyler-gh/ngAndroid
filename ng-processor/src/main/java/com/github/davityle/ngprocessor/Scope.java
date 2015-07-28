package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.xml.XmlScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

public class Scope {

    // TODO, Model classes, scopeType

    private final Element javaElement;
    private final Collection<Element> ngModelJavaElements;
    private final String scopeName;
    private final Map<String, XmlScope> xmlScopes;

    public Scope(Element javaElement, Collection<Element> ngModelJavaElements, String scopeName) {
        this.javaElement = javaElement;
        this.ngModelJavaElements = ngModelJavaElements;
        this.scopeName = scopeName;
        this.xmlScopes = new HashMap<>();
    }

    public void addXmlScope(String layout, XmlScope xmlScope) {
        xmlScopes.put(layout, xmlScope);
    }

    public Map<String, XmlScope> getXmlScopes() {
        return xmlScopes;
    }

    public Element getJavaElement() {
        return javaElement;
    }

    public Collection<Element> getNgModels() {
        return ngModelJavaElements;
    }

    public String getScopeName() {
        return scopeName;
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
