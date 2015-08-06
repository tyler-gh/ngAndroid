package com.github.davityle.ngprocessor.attributes;

import com.github.davityle.ngprocessor.util.Option;

public class Attribute extends AttrDependency {

    private final String attrName, classSource, attachSource;
    private String[] attrParameters;

    public Attribute(String className){
        this(className, toAttrName(className));
    }

    public Attribute(String className, Option<String> source) {
        super(className, source);
        this.attrName = toAttrName(className);
    }

    public Attribute(String className, String attrName) {
        super(className);
        this.attrName = attrName;
    }

    {
        this.classSource = getResource("attrs/class/" + getClassName() + "Class.java");
        this.attachSource = getResource("attrs/attach/" + getClassName() + "Attach.java");
    }

    public String getClassSource() {
        return classSource;
    }

    public String getAttachSource() {
        return attachSource;
    }

    public Attribute setAttrParameters(String ... attrParameters){
        this.attrParameters = attrParameters;
        return this;
    }

    public String[] getAttrParameters() {
        return attrParameters != null ? attrParameters : new String[0];
    }

    public String getAttrName() {
        return attrName;
    }

    private static String toAttrName(String className){
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
