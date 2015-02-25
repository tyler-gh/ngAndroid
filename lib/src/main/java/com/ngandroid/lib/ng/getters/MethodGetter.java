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

import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* Created by davityle on 1/24/15.
*/
public class MethodGetter implements Getter {

    private final Method mMethod;
    private final Object mScope;
    private final Getter[] mGetters;
    private final int type;

    public MethodGetter(Method method, Object scope, Getter... getters) {
        this.mMethod = method;
        this.mScope = scope;
        this.mGetters = getters;
        mMethod.setAccessible(true);
        type = TypeUtils.getType(method.getReturnType());
    }

    @Override
    public Object get() {
        Object[] parameters = new Object[mGetters.length];
        for(int index = 0; index < parameters.length; index++){
            try {
                parameters[index] = mGetters[index].get();
            } catch (Throwable throwable) {
                // TODO error
                throwable.printStackTrace();
            }
        }
        try {
            return mMethod.invoke(mScope, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO error
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getType() {
        return type;
    }

    public int getReturnType(){
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
