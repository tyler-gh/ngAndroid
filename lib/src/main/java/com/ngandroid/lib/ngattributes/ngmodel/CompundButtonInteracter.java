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

package com.ngandroid.lib.ngattributes.ngmodel;

import android.widget.CompoundButton;

import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ng.setters.ModelSetter;

/**
* Created by tyler on 2/24/15.
*/
final class CompundButtonInteracter implements CompoundButton.OnCheckedChangeListener, ModelMethod {
    private final ModelSetter modelSetter;
    private final CompoundButton compoundButton;
    private boolean isFromSelf;

    CompundButtonInteracter(ModelSetter modelSetter, CompoundButton compoundButton) {
        this.modelSetter = modelSetter;
        this.compoundButton = compoundButton;
    }

    @Override
    public Object invoke(String fieldName, Object... args) {
        try{
            if(!isFromSelf){
                isFromSelf = true;
                Boolean b = (Boolean) args[0];
                compoundButton.setChecked(b);
            }
        }finally {
            isFromSelf = false;
        }
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if(!isFromSelf) {
                isFromSelf = true;
                modelSetter.set(isChecked);
            }
        } catch (Throwable throwable) {
            // TODO - error
            throwable.printStackTrace();
        }finally {
            isFromSelf = false;
        }
    }
}
