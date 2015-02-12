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

import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by davityle on 1/12/15.
 */
public class MethodInvoker {

    private final Map<String, List<ModelMethod>> methodMap;
    private final Map<String, Tuple<Integer,Object>> fieldMap;

    public MethodInvoker(Map<String, List<ModelMethod>> methodMap, Map<String, Tuple<Integer,Object>> fieldMap) {
        this.methodMap = methodMap;
        this.fieldMap = fieldMap;
    }

    public Object invoke(String methodName, Object ... objects) throws Throwable{
        String fieldName = methodName.substring(3).toLowerCase();
        Tuple<Integer, Object> value = fieldMap.get(fieldName);
        if(value == null)
            return null;
        if(methodName.startsWith("get")){
            return value.getSecond();
        }else if(methodName.startsWith("set")){
            Object obj = objects[0];
            if(obj == TypeUtils.EMPTY){
                obj = TypeUtils.getEmptyValue(value.getFirst());
            }
            value.setSecond(obj);
            List<ModelMethod> setters = methodMap.get(methodName.toLowerCase());
            for(ModelMethod modelMethod : setters){
                modelMethod.invoke(fieldName, obj);
            }
            return null;
        }
        // TODO throw error
        return null;
    }

    public int getType(String fieldName){
        Tuple<Integer, Object> tuple = fieldMap.get(fieldName.toLowerCase());
        if(tuple == null)
            throw new RuntimeException("Field " + fieldName + " does not exist");
        return tuple.getFirst();
    }
}
