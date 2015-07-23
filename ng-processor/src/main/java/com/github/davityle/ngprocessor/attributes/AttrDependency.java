package com.github.davityle.ngprocessor.attributes;

import com.github.davityle.ngprocessor.util.Option;

import java.util.Scanner;

/**
 * Created by tyler on 7/3/15.
 */
public class AttrDependency {

    private final String className;
    private final Option<String> sourceCode;
    private String[] dependencies;

    public AttrDependency(String className) {
        this(className, Option.of(getResource("attributes/" + className + ".java")));
    }

    public AttrDependency(String className, Option<String> sourceCode) {
        this.className = className;
        this.sourceCode = sourceCode;
    }

    public String getClassName() {
        return className;
    }

    public Option<String> getSourceCode() {
        return sourceCode;
    }

    public AttrDependency setDependencies(String ... dependencies){
        this.dependencies = dependencies;
        return this;
    }

    public String[] getDependencies() {
        return dependencies != null ? dependencies : new String[0];
    }

    private static String getResource(String resourcePath){
        return new Scanner(AttrDependency.class.getClassLoader().getResourceAsStream(resourcePath), "UTF-8").useDelimiter("\\A").next();
    }
}
