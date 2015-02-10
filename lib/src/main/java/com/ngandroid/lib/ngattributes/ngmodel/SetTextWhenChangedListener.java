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

import android.text.Editable;
import android.text.TextWatcher;

import com.ngandroid.lib.ng.setters.ModelSetter;
import com.ngandroid.lib.utils.TypeUtils;

/**
* Created by davityle on 1/12/15.
*/
public class SetTextWhenChangedListener implements TextWatcher {
    private final ModelSetter mSetter;
    private final int mMethodType;
    private String mValidText = "";

    public SetTextWhenChangedListener(ModelSetter invocationHandler, int mMethodType) {
        this.mSetter = invocationHandler;
        this.mMethodType = mMethodType;
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override
    public void afterTextChanged(Editable editable) {
        Object value;
        String str;
        try {
            str = editable.toString();
            value = TypeUtils.fromStringEmptyStringIfEmpty(mMethodType, str);
            System.out.println(value.getClass().getSimpleName());
        } catch (Throwable e) {
            // TODO handle error | this replace does not actually work
            editable.replace(0, editable.length(), mValidText);
            e.printStackTrace();
            return;
        }
        mValidText = str;
        try{
            mSetter.set(value);
        } catch (Throwable e) {
            // TODO handle error
            e.printStackTrace();
        }
    }
}
