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

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by davityle on 1/12/15.
 */
public class ModelBuilder {
    private final Class mClass;
    private final Map<String, List<ModelMethod>> mMethodMap;
    private final Map<String, Tuple<Integer,Object>> mFieldMap;
    private final MethodInvoker mInvocationHandler;
    private final Method[] mModelMethods;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ModelBuilder(Class clzz) {
        this.mClass = clzz;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMethodMap = new ArrayMap<>();
            mFieldMap = new ArrayMap<>();
        } else {
            mMethodMap = new HashMap<>();
            mFieldMap = new HashMap<>();
        }
        mInvocationHandler = new MethodInvoker(mMethodMap, mFieldMap);
        mModelMethods = mClass.getDeclaredMethods();
        createFields();
    }

    private void createFields(){
        for(Method method : mModelMethods){
            String name = method.getName().toLowerCase();
            if(name.startsWith("set")){
                Class<?>[] parameters = method.getParameterTypes();
                if(parameters.length != 1){
                    throw new NgException("method set" + name + " must have single parameter");
                }
                createField(name.substring(3), parameters[0]);
            }
        }
        // a setter is required if there is a getter but a getter is not required if there is a setter
        for(Method method : mModelMethods){
            String name = method.getName().toLowerCase();
            if(name.startsWith("get") ){
                String field = name.substring(3).toLowerCase();
                if(hasField(field)) {
                    Class returnType = method.getReturnType();
                    if (TypeUtils.getType(returnType) != getFieldType(field)) {
                        throw new NgException("Getter and setter types do not match for field " + field);
                    }
                }else{
                    throw new NgException(field + " is missing it's setter");
                }
            }
        }
    }

    public Object create(){
        Set<Map.Entry<String, Tuple<Integer, Object>>> entries = mFieldMap.entrySet();
        for(Map.Entry<String, Tuple<Integer, Object>> entry : entries){
            Tuple<Integer, Object> value = entry.getValue();
            Object second = value.getSecond();
            if(second != null && second instanceof ModelBuilder){
                value.setSecond(((ModelBuilder)second).create());
            }
        }
        return Proxy.newProxyInstance(mClass.getClassLoader(), new Class[]{mClass}, new ModelInvokationHandler(mInvocationHandler));
    }

    public boolean hasField(String fieldNamelower){
        return mFieldMap.containsKey(fieldNamelower);
    }

    public int getFieldType(String fieldNamelower){
        return mFieldMap.get(fieldNamelower).getFirst();
    }

    public void setField(String fieldNamelower, int type,  Object defaultValue) {
        mFieldMap.put(fieldNamelower, Tuple.of(type, defaultValue));
    }

    public MethodInvoker getMethodInvoker(){
        return mInvocationHandler;
    }

    private void createField(String fieldName, Class clzz){
        final String fieldNamelower = fieldName.toLowerCase();
        int methodType = TypeUtils.getType(clzz);
        if(methodType != TypeUtils.OBJECT)
            setField(fieldNamelower, methodType, TypeUtils.getEmptyValue(methodType));
        else
            setField(fieldNamelower, methodType, (clzz.equals(mClass) ? null : new ModelBuilder(clzz)));
        mMethodMap.put("set" + fieldNamelower, new ArrayList<ModelMethod>());
    }

    public void addSetObserver(String fieldName, ModelMethod method){
        mMethodMap.get("set" + fieldName.toLowerCase()).add(method);
    }

    public static void buildModel(Object scope, ModelBuilderMap map){
        for(Map.Entry<String, com.ngandroid.lib.ng.ModelBuilder> entry : map.entrySet()){
            attachDynamicField(entry.getValue().create(), entry.getKey(), scope);
        }
    }

    private static void attachDynamicField(Object dynamicField, String modelName, Object scope){
        try {
            Field f = scope.getClass().getDeclaredField(modelName);
            f.setAccessible(true);
            f.set(scope, dynamicField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new NgException("There is not a field in scope '" + scope.getClass().getSimpleName() + "' called " + modelName);
        }
    }

    public Class<?> getFieldTypeClass(String keyLower) {
        for(Method method : mModelMethods){
            String name = method.getName().toLowerCase();
            if(name.toLowerCase().equals("set" + keyLower)){
                Class<?>[] parameters = method.getParameterTypes();
                if(parameters.length != 1){
                    throw new NgException("method 'set" + keyLower + "' must have single parameter");
                }
                return parameters[0];
            }
        }
        throw new NgException("method 'set" + keyLower + "' does not exist");
    }

    public void pullDefaults(Object model){
        for(Method method : mModelMethods){
            String name = method.getName().toLowerCase();
            if(name.startsWith("get")){
                method.setAccessible(true);
                try {
                    setField(name.substring(3).toLowerCase(), TypeUtils.getType(method.getReturnType()), method.invoke(model));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // TODO
                    e.printStackTrace();
                }
            }
        }
    }


    public static ModelBuilder createBuilder(Object scope, String modelName){
        try {
            System.out.println(modelName);
            Field f = scope.getClass().getDeclaredField(modelName);
            System.out.println(scope.getClass().getSimpleName());
            System.out.println(f.getType().getSimpleName());
            ModelBuilder builder = new ModelBuilder(f.getType());
            f.setAccessible(true);
            Object model = f.get(scope);
            if(model != null){
                System.out.println("Not null");
                builder.pullDefaults(model);
            }else{
                System.out.println("is null");
            }
            return builder;
        } catch (NoSuchFieldException e) {
            throw new NgException("There is not a model in scope '" + scope.getClass().getSimpleName() + "' called " + modelName);
        } catch (IllegalAccessException e) {
            throw new NgException(e);
        }
    }

}
