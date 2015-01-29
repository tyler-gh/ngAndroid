package com.ngandroid.lib.ng;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
* Created by davityle on 1/17/15.
*/
public class ModelBuilderMap extends HashMap<String, ModelBuilder> {
    private final Object model;

    public ModelBuilderMap(Object model) {
        this.model = model;
    }

    @Override
    public ModelBuilder get(Object key) {
        String modelName = (String) key;
        ModelBuilder builder = super.get(modelName);

        try {
            if(builder == null){
                Field f = model.getClass().getDeclaredField(modelName);
                builder = new ModelBuilder(f.getType(), model);
                put(modelName, builder);
            }
        } catch (NoSuchFieldException e) {
            // TODO rename error
            throw new RuntimeException("There is not a model in " + model.getClass().getSimpleName() + " called " + modelName);
        }
        return builder;
    }
}
