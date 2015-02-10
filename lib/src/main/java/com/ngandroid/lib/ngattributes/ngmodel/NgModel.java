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

package com.ngandroid.lib.ngattributes.ngmodel;

import android.view.View;
import android.widget.TextView;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.ng.setters.ModelSetter;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by davityle on 1/17/15.
 */
public class NgModel implements NgAttribute {
    private static NgModel ourInstance = new NgModel();

    public static NgModel getInstance() {
        return ourInstance;
    }

    private NgModel() {}

    public void typeCheck(Token[] tokens, Getter getter){
        TypeUtils.strictTypeCheck(tokens, TokenType.MODEL_NAME, TokenType.PERIOD, TokenType.MODEL_FIELD, TokenType.EOF);
    }

    public void attach(Getter getter, ModelBuilderMap modelBuilderMap, View bindView) throws Exception {
        if(!(getter instanceof ModelGetter)){
            throw new RuntimeException("You must only use models in ngModel");
        }
        ModelGetter modelGetter = (ModelGetter) getter;
        ModelBuilder modelBuilder = modelBuilderMap.get(modelGetter.getModelName());
        bindModelView(modelGetter, bindView, modelBuilder);
    }

    public void bindModelView(ModelGetter getter, View view, ModelBuilder builder) throws Exception {
        if(view instanceof  TextView){
            bindModelToTextView(getter, (TextView)view, builder);
        }
    }

    public void bindModelToTextView(ModelGetter getter, final TextView textView, ModelBuilder builder) throws Exception {
        final String fieldNamelower = getter.getFieldName();
        String defaultText =  textView.getText().toString();
        int methodType = getter.getType();
        builder.setField(fieldNamelower, methodType, TypeUtils.fromString(methodType, defaultText));
        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(new ModelSetter(fieldNamelower, builder.getMethodInvoker()), methodType);
        textView.addTextChangedListener(setTextWhenChangedListener);
        // TODO clean this up
        builder.addSetObserver(fieldNamelower, new ModelMethod(){
            @Override
            public Object invoke(String fieldName, Object... args) {
                String value = String.valueOf(args[0]);
                if(!value.equals(textView.getText().toString())) {
                    textView.removeTextChangedListener(setTextWhenChangedListener);
                    textView.setText(value.equals("0") ? "" : value);
                    textView.addTextChangedListener(setTextWhenChangedListener);
                }
                return null;
            }
        });
    }









}
