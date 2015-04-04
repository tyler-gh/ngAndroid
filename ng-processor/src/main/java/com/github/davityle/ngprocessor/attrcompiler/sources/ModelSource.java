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

package com.github.davityle.ngprocessor.attrcompiler.sources;


import java.util.List;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelSource implements Source {

    private final String modelName;
    private final String fieldName;
    private String method;

    public ModelSource(String modelName, String fieldName) {
        this.modelName = modelName;
        this.fieldName = fieldName;
    }

    @Override
    public String getSource() {
        if(method == null){
            throw new RuntimeException("Method not set for '" + modelName + '.' + fieldName + '\'');
        }
        return modelName + '.' + method + "()";
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        models.add(this);
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {}

    public String getModelName(){
        return modelName;
    }

    public String getFieldName(){
        return fieldName;
    }

    public void setMethod(String method){
        this.method = method;
    }
}
