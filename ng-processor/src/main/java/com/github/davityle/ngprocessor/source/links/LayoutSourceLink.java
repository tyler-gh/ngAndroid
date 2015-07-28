package com.github.davityle.ngprocessor.source.links;


import org.apache.velocity.VelocityContext;

import javax.lang.model.element.Element;

public class LayoutSourceLink implements SourceLink {

//    private final String layoutName;



    @Override
    public VelocityContext getVelocityContext() {
        return null;
    }

    @Override
    public Element[] getElements() {
        return new Element[0];
    }

    @Override
    public String getSourceFileName() {
        return null;
    }
}
