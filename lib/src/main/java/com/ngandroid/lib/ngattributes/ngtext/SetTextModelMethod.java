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

import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.interpreter.getters.Getter;

import java.lang.reflect.Method;

/**
* Created by tyler on 3/10/15.
*/
class SetTextModelMethod implements ModelMethod {
    private final Method method;
    private final View view;
    private final Getter<String> getter;

    public SetTextModelMethod(Method method, View view, Getter<String> getter) {
        this.method = method;
        this.view = view;
        this.getter = getter;
    }

    @Override
    public Object invoke(String fieldName, Object... args) {
        try {
            method.invoke(view, getter.get());
        } catch (Throwable throwable) {
            // TODO
            throwable.printStackTrace();
        }
        return null;
    }
}
