package com.ngandroid.lib.ng;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;
import android.widget.TextView;

import com.ngandroid.lib.ngmodel.NgModel;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davityle on 1/12/15.
 */
public class ModelBuilder {
    private final Class mClass;
    private final Map<String, List<ModelMethod>> mMethodMap;
    private final Map<String, Object> mFieldMap;
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

    public void ngModelBindTextView(NgModel ngModel, String fieldName, final TextView textView){
        ngModel.bindModelToTextView(fieldName, textView, mFieldMap, mMethodMap, mModelMethods, mInvocationHandler, mModel);
    }
}
