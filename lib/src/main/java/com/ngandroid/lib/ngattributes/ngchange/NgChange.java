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

package com.ngandroid.lib.ngattributes.ngchange;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ngandroid.lib.R;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.ng.Executor;
import com.ngandroid.lib.utils.Tuple;

/**
 * Created by tyler on 1/29/15.
 */
public class NgChange implements NgAttribute {

    private static NgChange ourInstance = new NgChange();

    public static NgChange getInstance() {
        return ourInstance;
    }

    private NgChange() {}

    @Override
    public void attach(Scope scope, View view, int layoutId, int viewId, Tuple<String, String>[] models){
        Executor executor = new Executor(scope, layoutId, viewId, getAttribute());
        if(view instanceof CompoundButton){
            CompoundButton button = (CompoundButton) view;
            button.setOnCheckedChangeListener(executor);
        }else if(view instanceof TextView){
            TextView textView = (TextView) view;
            textView.addTextChangedListener(executor);
        }else if(view instanceof Spinner){
            Spinner spinner = (Spinner) view;
            spinner.setOnItemSelectedListener(executor);
        }else if(view instanceof RadioGroup){
            RadioGroup group = (RadioGroup) view;
            group.setOnCheckedChangeListener(executor);
        }
    }

    @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngChange;
    }
}
