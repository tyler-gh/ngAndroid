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

package com.ngandroid.lib.ngattributes.ngtext;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;

/**
 * Created by tyler on 3/10/15.
 */
public class NgText implements NgAttribute {

    private static NgText ngText = new NgText();
    public static NgText getInstance(){
        return ngText;
    }

    private NgText(){}

    @Override
    public void typeCheck(Token[] tokens, Getter getter) throws Exception {
        if(getter.getType() != TypeUtils.STRING)
            throw new RuntimeException("NgText type must be STRING");
    }

    @Override
    public void attach(Getter getter, ModelGetter[] modelGetters, ModelBuilder[] modelBuilders, View view) throws Throwable {
        Method method = view.getClass().getDeclaredMethod("setText", CharSequence.class);
        for(int index = 0; index < modelGetters.length; index++){
            ModelGetter modelGetter = modelGetters[index];
            ModelBuilder builder = modelBuilders[index];
            builder.addSetObserver(modelGetter.getFieldName(), new SetTextModelMethod(method, view, getter));
        }
    }

}
