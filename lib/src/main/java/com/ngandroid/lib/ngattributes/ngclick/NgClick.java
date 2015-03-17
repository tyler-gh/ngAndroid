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

package com.ngandroid.lib.ngattributes.ngclick;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.MethodGetter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.ngattributes.nglongclick.LongClickInvoker;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by davityle on 1/23/15.
 */
public class NgClick implements NgAttribute {
    private static NgClick ourInstance = new NgClick();

    public static NgClick getInstance() {
        return ourInstance;
    }

    private NgClick() {
    }

    @Override
    public void typeCheck(Token[] tokens, Getter getter) {
        TypeUtils.startsWith(tokens, TokenType.FUNCTION_NAME);
        TypeUtils.endsWith(tokens, TokenType.CLOSE_PARENTHESIS);
    }

    @Override
    public void attach(Getter getter, ModelGetter[] modelGetters, ModelBuilder[] modelBuilders, View view) throws Throwable {
        attach(getter, view, false);
    }

    public void attach(Getter getter, View view, boolean isLongClick){
        MethodGetter methodGetter = (MethodGetter) getter;
        if(!isLongClick)
            view.setOnClickListener(new ClickInvoker(methodGetter));
        else
            view.setOnLongClickListener(new LongClickInvoker(methodGetter));
    }

}
