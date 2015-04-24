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

package com.ngandroid.lib.ngattributes.helpers;

import android.util.Log;
import android.widget.TextView;

import com.ngandroid.lib.ng.ModelObserver;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.utils.ValueFormatter;

/**
* Created by tyler on 3/10/15.
*/
public class SetTextModelObserver implements ModelObserver {


    private final Scope scope;
    private final int layoutId, viewId, attr;
    private final TextView hasText;
    private final ValueFormatter valueFormatter;

    public SetTextModelObserver(Scope scope, int layoutId, int viewId, int attr, TextView hasText, ValueFormatter valueFormatter) {
        this.scope = scope;
        this.layoutId = layoutId;
        this.viewId = viewId;
        this.attr = attr;
        this.hasText = hasText;
        this.valueFormatter = valueFormatter;
    }

    @Override
    public void invoke(String fieldName, Object arg) {
        try {
            Object value = scope.execute(layoutId, viewId, attr);
            if(value == null){
                Log.e("NgAndroid", "NgText value was null");
                return;
            }
            hasText.setText(valueFormatter.format(value));
        } catch (Throwable throwable) {
            // TODO
            throwable.printStackTrace();
        }
    }
}
