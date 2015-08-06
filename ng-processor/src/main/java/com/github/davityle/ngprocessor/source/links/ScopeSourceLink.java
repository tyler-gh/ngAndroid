package com.github.davityle.ngprocessor.source.links;

import com.github.davityle.ngprocessor.model.Scope;

import org.apache.velocity.VelocityContext;

import javax.lang.model.element.Element;

public class ScopeSourceLink implements SourceLink{

    private final Scope scope;
    private final String pack, className;
    private final String packageName;

    public ScopeSourceLink(Scope scope, String fullName, String packageName) {
        this.scope = scope;
        this.pack = fullName.substring(0, fullName.lastIndexOf('.'));
        this.className = fullName.substring(fullName.lastIndexOf('.') + 1) + "$$NgScope";
        this.packageName = packageName;
    }

    @Override
    public VelocityContext getVelocityContext() {
        VelocityContext vc = new VelocityContext();

        vc.put("scope", scope);
        vc.put("className", className);
        vc.put("package", pack);
        vc.put("packageName", packageName);
        return vc;
    }

    @Override
    public Element[] getElements() {
        return new Element[]{scope.getJavaElement()};
    }

    @Override
    public String getSourceFileName() {
        return pack + "." + className;
    }

    @Override
    public String toString() {
        return getSourceFileName() + ":" + scope.toString();
    }
}
