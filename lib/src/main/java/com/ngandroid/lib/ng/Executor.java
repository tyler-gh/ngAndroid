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

package com.ngandroid.lib.ng;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

/**
 * Created by tyler on 2/24/15.
 */
public class Executor implements
        View.OnClickListener,
        TextWatcher,
        AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnLongClickListener {

    private final Scope scope;
    private final int layoutId, viewId, attr;

    public Executor(Scope scope, int layoutId, int viewId, int attr) {
        this.scope = scope;
        this.layoutId = layoutId;
        this.viewId = viewId;
        this.attr = attr;
    }


    @Override
    public void onClick(View v) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        scope.execute(layoutId, viewId, attr);
    }

    @Override
    public boolean onLongClick(View v) {
        scope.execute(layoutId, viewId, attr);
        return false;
    }
}
