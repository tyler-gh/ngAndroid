package com.github.davityle.ngprocessor;

import java.util.Collection;

import javax.lang.model.element.Element;

public class Scope {

    private final Element javaElement;
    private final Collection<Element> ngModelJavaElements;

    public Scope(Element javaElement, Collection<Element> ngModelJavaElements) {
        this.javaElement = javaElement;
        this.ngModelJavaElements = ngModelJavaElements;
    }

    public Element getJavaElement() {
        return javaElement;
    }

    public Collection<Element> getNgModelJavaElements() {
        return ngModelJavaElements;
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
