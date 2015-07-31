package com.github.davityle.ngprocessor;

public class Model {

    private final String name, typeName;

    public Model(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public String getName(){
        return name;
    }

    public String getTypeName(){
        return typeName;
    }

}
