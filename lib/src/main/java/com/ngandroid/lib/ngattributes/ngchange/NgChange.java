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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;
import com.ngandroid.lib.ngattributes.ngclick.NgClick;
import com.ngandroid.lib.utils.TypeUtils;

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
    public void typeCheck(Token[] tokens) {
        TypeUtils.startsWith(tokens, TokenType.FUNCTION_NAME);
        TypeUtils.endsWith(tokens, TokenType.CLOSE_PARENTHESIS);
    }

    @Override
    public void attach(Token[] tokens, Object mModel, ModelBuilderMap builders, View bindView) throws Exception {
        typeCheck(tokens);
        final ClickInvoker invoker = NgClick.getInstance().getInvoker(tokens, mModel, builders, 0, 2);
        if(bindView instanceof CompoundButton){
            RadioButton button = (RadioButton) bindView;
            button.setOnClickListener(invoker);
        }else if(bindView instanceof TextView){
            TextView textView = (TextView) bindView;
            textView.addTextChangedListener(new TextChangedWatcher(invoker, textView));
        }else if(bindView instanceof Spinner){
            Spinner spinner = (Spinner) bindView;
            spinner.setOnItemSelectedListener(new SelectionChangeListener(invoker));
        }else if(bindView instanceof RadioGroup){
            RadioGroup group = (RadioGroup) bindView;
            group.setOnCheckedChangeListener(new CheckChangeListener(invoker));
        }
    }

}
