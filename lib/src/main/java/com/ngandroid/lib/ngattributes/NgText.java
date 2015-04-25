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
import android.widget.TextView;

import com.ngandroid.lib.R;
import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.ValueFormatter;


/**
 * Created by tyler on 3/10/15.
 */
class NgText implements NgAttribute {

    private final ValueFormatter valueFormatter;

    static NgText getInstance(ValueFormatter valueFormatter){
        return new NgText(valueFormatter);
    }

    private NgText(ValueFormatter valueFormatter){
        this.valueFormatter = valueFormatter;
    }

    @Override
    public void attach(Scope scope, View view, int layoutId, int viewId, Tuple<String, String>[] models) {
        // TODO make this more flexible
        if(view instanceof TextView) {
            TextView tv = (TextView) view;

            // TODO this may not be right and or useful...
            if(tv.getText().toString().isEmpty()){
                Object modelValue = scope.execute(layoutId, viewId, getAttribute());
                if(modelValue != null && !modelValue.toString().isEmpty()){
                    tv.setText(valueFormatter.format(modelValue));
                }
            }

            for (Tuple<String, String> model : models) {
                Model m = scope.getModel(model.getFirst());
                m.addObserver(model.getSecond(), new SetTextModelObserver(scope, layoutId, viewId, getAttribute(), tv, valueFormatter));
            }
        }
    }

    @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngText;
    }

//    @Override
//    public void typeCheck(Token[] tokens, Getter getter) throws Exception {
//        if(getter.getType() != TypeUtils.STRING)
//            throw new RuntimeException("NgText type must be STRING");
//    }
//
//    @Override
//    public void attach(Getter getter, ModelGetter[] modelGetters, Model[] models, View view) throws Throwable {
//        Method method = view.getClass().getDeclaredMethod("setText", CharSequence.class);
//        for(int index = 0; index < modelGetters.length; index++){
//            ModelGetter modelGetter = modelGetters[index];
//            Model model = models[index];
//            model.addObserver(modelGetter.getFieldName(), new SetTextModelMethod(method, view, getter));
//        }
//    }

}
