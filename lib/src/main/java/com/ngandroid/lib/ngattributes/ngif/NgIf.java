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

package com.ngandroid.lib.ngattributes.ngif;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by tyler on 2/10/15.
 */
public abstract class NgIf implements NgAttribute {
    @Override
    public void typeCheck(Token[] tokens, Getter getter) throws Exception {
        // TODO - error
        if(getter.getType() != TypeUtils.BOOLEAN)
            throw new Exception(getClass().getSimpleName() + " requires a boolean type model");

    }

    @Override
    public void attach(Getter getter, ModelGetter[] modelGetters, ModelBuilder[] modelBuilders, View view) throws Throwable {
        if(modelGetters.length == 0)
            throw new Exception(getClass().getSimpleName() +" requires a model to observe. Try reformatting your statement to include a model");
        for(int index = 0 ; index < modelGetters.length; index++){
            ModelGetter modelGetter = modelGetters[index];
            ModelBuilder modelBuilder = modelBuilders[index];
            modelBuilder.addSetObserver(modelGetter.getFieldName(), getModelMethod(getter, view));
        }
    }

//    public void attach(Getter getter, ModelBuilderMap modelBuilderMap, View view) throws Exception {
//        if(!observeModels(getter, getter, modelBuilderMap, view)){
//            // TODO - error
//            throw new Exception(getClass().getSimpleName() +" requires a model to observe. Try reformatting your statement to include a model");
//        }
//    }

    protected abstract ModelMethod getModelMethod(Getter<Boolean> getter, View view);

}
