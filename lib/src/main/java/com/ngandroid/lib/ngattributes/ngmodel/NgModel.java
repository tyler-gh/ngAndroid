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

import com.ngandroid.lib.R;
import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.utils.Tuple;
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

    @Override
    public void attach(Scope scope, View view, int layoutId, int viewId, Tuple<String, String>[] models) {
        // TODO make a compile error if NgModel does not contain a model
        Model model = scope.getModel(models[0].getFirst());
        String field = models[0].getSecond();
        if(view instanceof CompoundButton){
            bindModelToCompoundButton((CompoundButton) view, model, field);
        }else if(view instanceof TextView){
            bindModelToTextView((TextView)view, model, field);
        }
    }

    private void bindModelToCompoundButton(final CompoundButton compoundButton, Model model, String field) {
        if(TypeUtils.getType(model.getType(field)) != TypeUtils.BOOLEAN){
            throw new NgException("A compound button requires a boolean type model");
        }
        boolean isChecked = compoundButton.isChecked();
        model.setValue(field, isChecked);
        CompundButtonInteracter compundButtonInteracter = new CompundButtonInteracter(model, field, compoundButton);
        compoundButton.setOnCheckedChangeListener(compundButtonInteracter);
        model.addObserver(field, compundButtonInteracter);
    }

    private void bindModelToTextView(final TextView textView, Model model, String field) {
        String defaultText =  textView.getText().toString();
        if(!defaultText.isEmpty())
            model.setValue(field, TypeUtils.fromString(TypeUtils.getType(model.getType(field)), defaultText));
        final TextInteracter textInteracter = new TextInteracter(model, field, textView);
        textView.addTextChangedListener(textInteracter);
        model.addObserver(field, textInteracter);
    }


        @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngModel;
    }
}
