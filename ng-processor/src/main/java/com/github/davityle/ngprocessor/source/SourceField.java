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

package com.github.davityle.ngprocessor.source;

import com.github.davityle.ngprocessor.util.PrimitiveUtils;

/**
* Created by tyler on 3/25/15.
*/
public final class SourceField {

    private final String name, typeName;
    private final PrimitiveUtils primitiveUtils;
    private String getter, setter;

    public SourceField(String name, String typeName, PrimitiveUtils primitiveUtils) {
        this.name = name;
        this.primitiveUtils = primitiveUtils;
        this.typeName = typeName.replaceAll("<.*>", "");
    }

    public String getName(){
        return name;
    }

    public String getTypeName(){
        return typeName;
    }

    public String getObjectType(){
        return primitiveUtils.getObjectType(typeName);
    }

    public String getGetter() { return getter; }

    public String getSetter() { return setter; }

    public boolean isPrimitive() {
        return primitiveUtils.isPrimitive(typeName);
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
