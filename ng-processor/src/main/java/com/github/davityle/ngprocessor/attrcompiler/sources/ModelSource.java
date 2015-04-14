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


import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelSource extends Source<ModelSource> {

    private final String modelName;
    private final String fieldName;
    private String method;

    public ModelSource(String modelName, String fieldName) {
        this(modelName, fieldName, null);
    }

    public ModelSource(String modelName, String fieldName, TypeMirror typeMirror) {
        super(typeMirror);
        this.modelName = modelName;
        this.fieldName = fieldName;
    }

    @Override
    public String getSource() {
        // TODO
//        if(method == null){
//            throw new RuntimeException("Method not set for '" + modelName + '.' + fieldName + '\'');
//        }
//        return modelName + '.' + method + "()";
        // TODO
        return "scope." + modelName + '.' + "get" + capitalize(fieldName) + "()";
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        models.add(this);
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {}

    @Override
    protected ModelSource cp(TypeMirror typeMirror) {
        TypeMirror current = getTypeMirror();
        if(current != null && !TypeUtils.match(current, typeMirror))
            throw new IllegalArgumentException(current + " cannot be assigned to " + typeMirror);
        return new ModelSource(modelName, fieldName, typeMirror);
    }

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
