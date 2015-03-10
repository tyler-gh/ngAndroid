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

package com.ngandroid.lib.ng.getters;

import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by tyler on 2/10/15.
 */
public class KnotGetter implements Getter<Boolean>{

    private final Getter<Boolean> getter;

    public KnotGetter(Getter<Boolean> getter) {
        this.getter = getter;
        if(getter.getType() != TypeUtils.BOOLEAN){
            throw new NgException("A negation(!) may only negate a boolean expression");
        }
    }


    @Override
    public Boolean get() throws Throwable {
        return !getter.get();
    }

    @Override
    public int getType() {
        return TypeUtils.BOOLEAN;
    }

    public Getter<Boolean> getBooleanGetter(){
        return getter;
    }

}
