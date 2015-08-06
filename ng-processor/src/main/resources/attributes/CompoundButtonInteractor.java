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

import android.util.Log;
import android.widget.CompoundButton;

import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelObserver;

final class CompoundButtonInteractor implements CompoundButton.OnCheckedChangeListener, ModelObserver {

    private final Model model;
    private final String field;
    private final CompoundButton compoundButton;
    private boolean isFromSelf;

    public CompoundButtonInteractor(Model model, String field, CompoundButton compoundButton) {
        this.model = model;
        this.field = field;
        this.compoundButton = compoundButton;
    }

    @Override
    public void invoke(String fieldName, Object arg) {
        try{
            if(!isFromSelf){
                isFromSelf = true;
                Boolean b = (Boolean) arg;
                compoundButton.setChecked(b);
            }
        }finally {
            isFromSelf = false;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if(!isFromSelf) {
                isFromSelf = true;
                model.setValue(field, isChecked);
            }
        } catch (Throwable throwable) {
            Log.e("CompoundButton", "An error was thrown while setting the value on a model.", throwable);
        }finally {
            isFromSelf = false;
        }
    }
}
