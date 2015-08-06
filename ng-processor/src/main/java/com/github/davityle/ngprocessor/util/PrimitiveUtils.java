package com.github.davityle.ngprocessor.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class PrimitiveUtils {

    private static final Map<String, String> primitives = new HashMap<String, String>(){{
        put("byte", "Byte");
        put("short", "Short");
        put("int", "Integer");
        put("long", "Long");
        put("float", "Float");
        put("double", "Double");
        put("char", "Character");
        put("boolean", "Boolean");
    }};

    @Inject
    public PrimitiveUtils() {}

    public boolean isPrimitive(String type) {
        return primitives.containsKey(type);
    }

    public String getWrapper(String type) {
        return primitives.get(type);
    }

    public String getObjectType(String type) {
        if(isPrimitive(type)) {
            return getWrapper(type);
        }
        return type;
    }
}
