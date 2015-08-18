package com.github.davityle.ngprocessor.source.links;


import com.github.davityle.ngprocessor.model.Layout;
import com.github.davityle.ngprocessor.model.Scope;

import org.apache.velocity.VelocityContext;

import java.util.Collection;
import java.util.Iterator;

import javax.lang.model.element.Element;

public class LayoutSourceLink implements SourceLink {

    private final static String PACKAGE = "ng.layout";

    private final Collection<Scope> scopes;
    private final String layoutPath, layoutName, className, packageName, javaName;

    public LayoutSourceLink(Collection<Scope> scopes, Layout layout, String packageName) {
        this.scopes = scopes;
        this.layoutPath = layout.getPath();
        this.layoutName = layout.getFileName();
        this.javaName = layout.getJavaName();
        this.className = layout.getJavaName() + "Controller";
        this.packageName = packageName;
    }

    @Override
    public VelocityContext getVelocityContext() {
        VelocityContext vc = new VelocityContext();

        vc.put("layoutName", layoutName);
        vc.put("className", className);
        vc.put("javaName", javaName);
        vc.put("scopes", scopes);
        vc.put("layoutPath", layoutPath);
        vc.put("package", PACKAGE);
        vc.put("packageName", packageName);
        return vc;
    }

    @Override
    public Element[] getElements() {
        Element[] elements = new Element[scopes.size()];
        Iterator<Scope> it = scopes.iterator();
        int i = 0;
        while(it.hasNext()) {
            elements[i] = it.next().getJavaElement();
            i++;
        }
        return elements;
    }

    @Override
    public String getSourceFileName() {
        return PACKAGE + "." + className;
    }

    @Override
    public String toString() {
        return layoutPath;
    }
}
