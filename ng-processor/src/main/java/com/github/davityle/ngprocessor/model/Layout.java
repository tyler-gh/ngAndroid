package com.github.davityle.ngprocessor.model;

import java.io.File;

public class Layout {

    private final String path, javaName, fileName;

    public Layout(String path) {
        this.path = path;
        this.fileName = fileName(path);
        this.javaName = name(path);
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getJavaName() {
        return javaName;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Layout && ((Layout)obj).path.equals(path);
    }

    private static String fileName(String path) {
        return path.substring(path.lastIndexOf(File.separatorChar) + 1).replace(".xml", "");
    }

    private static String name(String path) {
        String[] sections = fileName(path).split("_");
        StringBuilder name = new StringBuilder();
        for(String s : sections) {
            if(s.length() > 0) {
                name.append(Character.toUpperCase(s.charAt(0)));
            }
            if(s.length() > 1){
                name.append(s.substring(1));
            }
        }
        return name.toString();
    }
}
