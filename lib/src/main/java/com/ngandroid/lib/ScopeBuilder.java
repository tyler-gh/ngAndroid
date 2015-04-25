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

package com.ngandroid.lib;

import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.ng.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class ScopeBuilder {
    private static final String SCOPE_APPEN = "$$NgScope";
    private static final Map<String, Constructor<? extends Scope>> CLASSES = new HashMap<>();

    static Scope getScope(Object scope, NgAndroid ngAndroid){
        String scopeName = scope.getClass().getName() + SCOPE_APPEN;
        Constructor<? extends Scope> constructor = CLASSES.get(scopeName);
        if(constructor == null){
            try {
                Class<? extends Scope> scopeClass = (Class<? extends Scope>) Class.forName(scopeName);
                constructor = scopeClass.getConstructor(scope.getClass(), NgAndroid.class);
                CLASSES.put(scopeName, constructor);
            } catch (ClassNotFoundException e) {
                throw new NgException("Scope " + scopeName + " not found", e);
            } catch (ClassCastException e){
                throw new NgException(scopeName + " does not extend Scope", e);
            } catch (NoSuchMethodException e) {
                throw new NgException("Correct constructor for '" +scopeName + "' does not exist", e);
            }
        }
        try {
            return constructor.newInstance(scope, ngAndroid);
        } catch (InstantiationException e) {
            throw new NgException("Unable to instantiate scope", e);
        } catch (IllegalAccessException e) {
            throw new NgException("Unable to access scope", e);
        } catch (InvocationTargetException e) {
            throw new NgException(e);
        }
    }
}
