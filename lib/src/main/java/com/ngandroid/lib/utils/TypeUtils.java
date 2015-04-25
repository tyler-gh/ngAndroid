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

package com.ngandroid.lib.utils;

/**
 * DO NOT USE. This is for NgAndroid internal use.
 */
public class TypeUtils {
    /**
     * DO NOT USE. This is for NgAndroid internal use.
     */
    public static final int INTEGER = 0, LONG = 1, STRING = 2, DOUBLE = 3, FLOAT = 4, BOOLEAN = 7, OBJECT = 8;

    /**
     * DO NOT USE. This is for NgAndroid internal use.
     */
    public static int getType(Class<?> clzz){
        if(int.class.equals(clzz) || Integer.class.equals(clzz))
            return INTEGER;
        if(long.class.equals(clzz) || Long.class.equals(clzz))
            return LONG;
        if(String.class.equals(clzz))
            return STRING;
        if(double.class.equals(clzz) || Double.class.equals(clzz))
            return DOUBLE;
        if(float.class.equals(clzz) || Float.class.equals(clzz))
            return FLOAT;
        if(boolean.class.equals(clzz) || Boolean.class.equals(clzz))
            return BOOLEAN;
        return OBJECT;
    }

    /**
     * DO NOT USE. This is for NgAndroid internal use.
     */
    public static Object fromString(int type, String value) {
        if(!value.trim().isEmpty()) {
            switch (type) {
                case INTEGER:
                    return Integer.parseInt(value);
                case LONG:
                    return Long.parseLong(value);
                case DOUBLE:
                    return Double.parseDouble(value);
                case FLOAT:
                    return Float.parseFloat(value);
                case BOOLEAN:
                    return Boolean.parseBoolean(value);
                case STRING:
                case OBJECT:
                    // TODO what to do here with object?
                default:
                    return value;
            }
        }else{
            return getEmptyValue(type);
        }
    }

    /**
     * DO NOT USE. This is for NgAndroid internal use.
     */
    public static Object getEmptyValue(int type){
        switch (type) {
            case INTEGER:
                return 0;
            case LONG:
                return 0l;
            case DOUBLE:
                return 0d;
            case FLOAT:
                return 0f;
            case BOOLEAN:
                return false;
            case STRING:
                return "";
            case OBJECT:
                // TODO what to do here with object?
            default:
                return null;
        }
    }
}
