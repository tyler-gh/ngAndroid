package com.ngandroid.lib.ngbind;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;
import android.widget.TextView;

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
public class BindingHandlerBuilder {
    private final Class mClass;
    private final Map<String, List<BindingMethod>> mMethodMap;
    private final Map<String, Object> mFieldMap;
    private final Object mModel;
    private final MethodInvoker mInvocationHandler;
    private final Method[] mModelMethods;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public BindingHandlerBuilder(Class clzz, Object model) {
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
        return Proxy.newProxyInstance(mClass.getClassLoader(), new Class[]{mClass}, new BindingHandler(mInvocationHandler));
    }

    public void addTextViewBind(String fieldName, final TextView textView){
        final String fieldNamelower = fieldName.toLowerCase();
        String defaultText =  textView.getText().toString();
        mFieldMap.put(fieldNamelower, defaultText);
        int methodType = TypeUtils.STRING;
        for(Method m : mModelMethods){
            if(m.getName().toLowerCase().equals("set" + fieldNamelower)){
                methodType = TypeUtils.getType(m.getParameterTypes()[0]);
            }
        }
        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(fieldNamelower, mInvocationHandler, mModel, methodType);
        textView.addTextChangedListener(setTextWhenChangedListener);
        BindingMethod method = new BindingMethod(){
            @Override
            public Object invoke(String fieldName, Object... args) {
                String value = String.valueOf(args[0]);
                if(!value.equals(textView.getText().toString())) {
                    textView.removeTextChangedListener(setTextWhenChangedListener);
                    textView.setText(value);
                    textView.addTextChangedListener(setTextWhenChangedListener);
                }
                return null;
            }
        };
        String methodName = "set" + fieldName.toLowerCase();
        List<BindingMethod> methods = mMethodMap.get(methodName);
        if(methods == null){
            methods = new ArrayList<>();
            mMethodMap.put(methodName, methods);
        }
        methods.add(method);
    }
}
