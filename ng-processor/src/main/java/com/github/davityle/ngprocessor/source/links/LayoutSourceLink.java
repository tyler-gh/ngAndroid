package com.github.davityle.ngprocessor.source.links;


import com.github.davityle.ngprocessor.Scope;
import com.github.davityle.ngprocessor.source.SourceField;

import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.lang.model.element.Element;

public class LayoutSourceLink implements SourceLink {

    private final static String PACKAGE = "ng.layout";

    private final Collection<Scope> scopes;
    private final String layoutPath, layoutName, className, packageName;

    public LayoutSourceLink(Collection<Scope> scopes, String layoutPath, String packageName) {
        this.scopes = scopes;
        this.layoutPath = layoutPath;
        this.layoutName = layoutPath.substring(layoutPath.lastIndexOf(File.separatorChar) + 1).replace(".xml", "");
        this.className = "Ng" + SourceField.capitalize(layoutName);
        this.packageName = packageName;
    }

    @Override
    public VelocityContext getVelocityContext() {
        VelocityContext vc = new VelocityContext();

        vc.put("layoutName", layoutName);
        vc.put("className", className);
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
