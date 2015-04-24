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

import com.ngandroid.lib.R;
import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelObserver;
import com.ngandroid.lib.ngattributes.helpers.FireCheckObserver;

/**
 * Created by tyler on 2/10/15.
 */
public class NgGone extends NgIf {

    private static NgGone ngGone = new NgGone();

    private NgGone(){}

    static NgGone getInstance() {
        return ngGone;
    }

    @Override
    protected ModelObserver getModelMethod(Model model, View view, String field) {
        return new FireCheckObserver(model, view, field, true);
    }

    @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngIf;
    }
}
