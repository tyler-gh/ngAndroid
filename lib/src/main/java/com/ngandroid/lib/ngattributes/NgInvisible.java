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

/**
 * Created by tyler on 2/10/15.
 */
class NgInvisible extends NgIf {
    private static NgInvisible ngInvisible = new NgInvisible();
    private NgInvisible(){}

    static NgInvisible getInstance() {
        return ngInvisible;
    }

    @Override
    protected ModelObserver getModelMethod(Model model, View view, String field) {
        return new FireCheckObserver(model, view, field, false);
    }

    @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngInvisible;
    }
}
