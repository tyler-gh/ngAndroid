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

package com.ngandroid.lib.ngattributes.ngif;

import android.view.View;

import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ng.getters.Getter;

/**
* Created by tyler on 2/10/15.
*/
final class FireCheckObserver implements ModelMethod {

    private final Getter<Boolean> getter;
    private final View view;
    private final boolean isGone;

    FireCheckObserver(Getter<Boolean> getter, View view, boolean isGone) {
        this.getter = getter;
        this.view = view;
        this.isGone = isGone;
    }

    @Override
    public Object invoke(String fieldName, Object... args) {
        try {
            if(getter.get()){
                if(isGone)
                    view.setVisibility(View.GONE);
                else
                    view.setVisibility(View.INVISIBLE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        } catch (Throwable throwable) {
            // TODO - error
            throwable.printStackTrace();
        }
        return null;
    }
}
