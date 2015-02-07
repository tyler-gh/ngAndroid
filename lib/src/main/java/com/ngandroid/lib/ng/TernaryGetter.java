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

/**
 * Created by tyler on 2/2/15.
 */
public class TernaryGetter<T> implements Getter {

    private final Getter<Boolean> booleanGetter;
    private final Getter<T> valTrue, valFalse;

    public TernaryGetter(Getter<Boolean> booleanGetter, Getter<T> valTrue, Getter<T> valFalse) {
        this.booleanGetter = booleanGetter;
        this.valTrue = valTrue;
        this.valFalse = valFalse;
    }

    @Override
    public T get() throws Throwable {
        return booleanGetter.get() ? valTrue.get() : valFalse.get();
    }

    @Override
    public int getType() {
        return valTrue.getType();
    }
}
