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

package com.ngandroid.lib.ngattributes;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ngandroid.lib.R;
import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;
import com.ngandroid.lib.utils.ValueFormatter;

/**
 * Created by davityle on 1/17/15.
 */
class NgModel implements NgAttribute {
    private final ValueFormatter valueFormatter;

    static NgModel getInstance(ValueFormatter valueFormatter) {
        return new NgModel(valueFormatter);
    }

    private NgModel(ValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

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
        final TextInteracter textInteracter = new TextInteracter(model, field, textView, valueFormatter);
        textView.addTextChangedListener(textInteracter);
        model.addObserver(field, textInteracter);
    }


        @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngModel;
    }
}
