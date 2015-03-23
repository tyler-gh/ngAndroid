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

package com.ngandroid.lib.ngattributes.ngfocus;

import android.view.View;

import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.interpreter.getters.Getter;
import com.ngandroid.lib.ngattributes.ngif.NgIf;

/**
 * Created by tyler on 2/17/15.
 */
public class NgFocus extends NgIf {

    private static NgFocus ngFocus = new NgFocus();
    private NgFocus(){}

    public static NgFocus getInstance(){return ngFocus;}

    @Override
    protected ModelMethod getModelMethod(final Getter<Boolean> getter, final View view) {
        return new ModelMethod() {
            @Override
            public Object invoke(String fieldName, Object... args) {
                try {
                    if(getter.get()){
                        view.requestFocus();
                    }else{
                        view.clearFocus();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        };
    }
}
