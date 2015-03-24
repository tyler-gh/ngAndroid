/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ngandroid.lib.interpreter.getters;

import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.utils.TypeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelGetter<T> implements Getter<T> {

    private final String fieldName;
    private final String modelName;
    private final Model model;
    private final int type;

    public ModelGetter(String fieldName, String modelName, Model model) {
        this.fieldName = fieldName;
        this.modelName = modelName;
        this.model = model;
        this.type = TypeUtils.getType(model.getType(fieldName));
    }

    public T get() throws Throwable {
        return (T) model.getValue(fieldName);
    }

    public int getType(){
        return type;
    }

    @Override
    public void getModelGetter(List<ModelGetter> modelGetters) {
        modelGetters.add(this);
    }

    public String getFieldName(){
        return fieldName;
    }

    public String getModelName() { return modelName; }

    public static ModelGetter[] getModelGetters(Getter getter){
        List<ModelGetter> mgs = new ArrayList<>();
        getter.getModelGetter(mgs);
        return mgs.toArray(new ModelGetter[mgs.size()]);
    }

    public static Model[] getModels(ModelGetter[] modelGetters, Scope modelBuilderMap){
        Model[] builders = new Model[modelGetters.length];
        for(int index = 0; index < modelGetters.length; index++){
            builders[index] = modelBuilderMap.getModel(modelGetters[index].getModelName());
        }
        return builders;
    }
}
