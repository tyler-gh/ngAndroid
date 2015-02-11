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

package com.ngandroid.lib.ngattributes.ngform;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;

/**
 * Created by tyler on 2/10/15.
 */
public class NgForm implements NgAttribute{


    @Override
    public void typeCheck(Token[] tokens, Getter getter) throws Exception {

    }

    @Override
    public void attach(Getter getter, ModelBuilderMap modelBuilderMap, View view) throws Exception {

    }
}
