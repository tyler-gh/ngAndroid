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

import java.util.HashMap;

/**
* Created by davityle on 1/17/15.
*/
public class ModelBuilderMap extends HashMap<String, ModelBuilder> {
    private final Object scope;

    public ModelBuilderMap(Object scope) {
        this.scope = scope;
    }

    @Override
    public ModelBuilder get(Object key) {
        String modelName = (String) key;
        ModelBuilder builder = super.get(modelName);
        if(builder == null){
            builder = ModelBuilder.createBuilder(scope, modelName);
            put(modelName, builder);
        }
        return builder;
    }
}
