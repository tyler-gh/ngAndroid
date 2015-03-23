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

import com.ngandroid.lib.interpreter.getters.MethodGetter;

/**
 * Created by tyler on 2/24/15.
 */
public class ClickInvoker implements View.OnClickListener {

    private final MethodGetter mMethodGetter;

    public ClickInvoker(MethodGetter methodGetter) {
        this.mMethodGetter = methodGetter;
    }

    @Override
    public void onClick(View v) {
        mMethodGetter.get();
    }
}
