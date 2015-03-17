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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilder;
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

    @Override
    public void attach(Getter getter, ModelGetter[] modelGetters, ModelBuilder[] modelBuilders, View view) throws Throwable {
        bindModelView((ModelGetter) getter, view, modelBuilders[0]);
    }

//    public void attach(Getter getter, ModelBuilderMap modelBuilderMap, View bindView) throws Throwable {
//        if(!(getter instanceof ModelGetter)){
//            throw new RuntimeException("You must only use models in ngModel");
//        }
//        ModelGetter modelGetter = (ModelGetter) getter;
//        ModelBuilder modelBuilder = modelBuilderMap.get(modelGetter.getModelName());
//        bindModelView(modelGetter, bindView, modelBuilder);
//    }

    public void bindModelView(ModelGetter getter, View view, ModelBuilder builder) throws Throwable {
        if(view instanceof CompoundButton){
            bindModelToCompoundButton(getter, (CompoundButton) view, builder);
        }else if(view instanceof  TextView){
            bindModelToTextView(getter, (TextView)view, builder);
        }
    }
    private void bindModelToCompoundButton(ModelGetter<Boolean> getter, final CompoundButton compoundButton, ModelBuilder builder) throws Throwable {
        if(getter.getType() != TypeUtils.BOOLEAN){
            // TODO error
            throw new RuntimeException("A compound button requires a boolean type model");
        }
        boolean isChecked = compoundButton.isChecked();
        String fieldName = getter.getFieldName().toLowerCase();
        final ModelSetter modelSetter = new ModelSetter(fieldName, builder.getMethodInvoker());
        modelSetter.set(isChecked);
        CompundButtonInteracter compundButtonInteracter = new CompundButtonInteracter(modelSetter, compoundButton);
        compoundButton.setOnCheckedChangeListener(compundButtonInteracter);
        builder.addSetObserver(fieldName, compundButtonInteracter);
    }


    private void bindModelToTextView(ModelGetter getter, final TextView textView, ModelBuilder builder) throws Throwable {
        final String fieldNamelower = getter.getFieldName();
        String defaultText =  textView.getText().toString();
        int methodType = getter.getType();
        ModelSetter setter = new ModelSetter(fieldNamelower, builder.getMethodInvoker());
        if(!defaultText.isEmpty())
            setter.set(TypeUtils.fromString(methodType, defaultText));
        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(setter, methodType);
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
