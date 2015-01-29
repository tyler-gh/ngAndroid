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

import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davityle on 1/12/15.
 */
public class ModelBuilder {
    private final Class mClass;
    private final Map<String, List<ModelMethod>> mMethodMap;
    private final Map<String, Tuple<Integer,Object>> mFieldMap;
    private final Object mModel;
    private final MethodInvoker mInvocationHandler;
    private final Method[] mModelMethods;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ModelBuilder(Class clzz, Object model) {
        this.mClass = clzz;
        this.mModel = model;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMethodMap = new ArrayMap<>();
            mFieldMap = new ArrayMap<>();
        } else {
            mMethodMap = new HashMap<>();
            mFieldMap = new HashMap<>();
        }
        mInvocationHandler = new MethodInvoker(mMethodMap, mFieldMap);
        mModelMethods = mClass.getDeclaredMethods();
    }

    public Object create(){
        return Proxy.newProxyInstance(mClass.getClassLoader(), new Class[]{mClass}, new Model(mInvocationHandler));
    }

    public void setField(String fieldNamelower, int type,  Object defaultValue) {
        mFieldMap.put(fieldNamelower, Tuple.of(type, defaultValue));
    }

    public MethodInvoker getMethodInvoker(){
        return mInvocationHandler;
    }

    public int getMethodType(String fieldNamelower) {
        int methodType = TypeUtils.STRING;
        for(Method m : mModelMethods){
            if(m.getName().toLowerCase().equals("set" + fieldNamelower)){
                methodType = TypeUtils.getType(m.getParameterTypes()[0]);
                break;
            }
        }
        return methodType;
    }

    public List<ModelMethod> getMethods(String methodName) {
        List<ModelMethod> methods = mMethodMap.get(methodName);
        if(methods == null){
            methods = new ArrayList<>();
            mMethodMap.put(methodName, methods);
        }
        return  methods;
    }
}
