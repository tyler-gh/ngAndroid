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

package com.ngandroid.lib.ngattributes.ngchange;

import android.view.View;
import android.widget.AdapterView;

import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;

/**
* Created by tyler on 1/29/15.
*/
class SelectionChangeListener implements AdapterView.OnItemSelectedListener {
    private final ClickInvoker invoker;

    public SelectionChangeListener(ClickInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        invoker.onClick(selectedItemView);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        invoker.onClick(null);
    }

}
