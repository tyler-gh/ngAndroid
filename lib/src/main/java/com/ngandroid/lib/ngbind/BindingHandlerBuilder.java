package com.ngandroid.lib.ngbind;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davityle on 1/12/15.
 */
public class BindingHandlerBuilder {
    private final Class clzz;
    private final Map<String, List<BindingMethod>> methodMap;
    private final Map<String, Object> fieldMap;
    private final Object model;

    private final Invoker invocationHandler = new Invoker() {
        @Override
        public Object invoke(Object o, String methodName, Object[] objects) throws Throwable {
            if(methodName.startsWith("get")){
                String fieldName = methodName.substring(3).toLowerCase();
                return fieldMap.get(fieldName);
            }else if(methodName.startsWith("set")){
                String fieldName = methodName.substring(3).toLowerCase();
                fieldMap.put(fieldName, o);
                List<BindingMethod> setters = methodMap.get(methodName.toLowerCase());
                for(BindingMethod bindingMethod : setters){
                    bindingMethod.invoke(fieldName, objects);
                }
                return null;
            }else{
                // TODO throw error
                return null;
            }
        }
    };

    private class SetTextWhenChangedListener implements TextWatcher {
        private final String fieldName;

        private SetTextWhenChangedListener(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override
        public void afterTextChanged(Editable editable) {
            try {
                invocationHandler.invoke(model, "set" + fieldName, new Object[]{editable.toString()});
            } catch (Throwable throwable) {
                // TODO handle error
                throwable.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public BindingHandlerBuilder(Class clzz, Object model) {
        this.clzz = clzz;
        this.model = model;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            methodMap = new ArrayMap<>();
            fieldMap = new ArrayMap<>();
        } else {
            methodMap = new HashMap<>();
            fieldMap = new HashMap<>();
        }
    }

    public Object create(){
        return Proxy.newProxyInstance(clzz.getClassLoader(), new Class[]{clzz}, new BindingHandler(invocationHandler));
    }

    public void addTextViewBind(String fieldName, final TextView textView){
        final String fieldNamelower = fieldName.toLowerCase();
        fieldMap.put(fieldNamelower, textView.getText().toString().toLowerCase());

        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(fieldNamelower);
        textView.addTextChangedListener(setTextWhenChangedListener);
        BindingMethod method = new BindingMethod(){
            @Override
            public Object invoke(String fieldName, Object... args) {
                textView.removeTextChangedListener(setTextWhenChangedListener);
                textView.setText((String) args[0]);
                textView.addTextChangedListener(setTextWhenChangedListener);
                return null;
            }
        };
        String methodName = "set" + fieldName.toLowerCase();
        List<BindingMethod> methods = methodMap.get(methodName);
        if(methods == null){
            methods = new ArrayList<>();
            methodMap.put(methodName, methods);
        }
        methods.add(method);
    }

    public void addEditTextBind(String fieldName, final EditText editText){
        final String fieldNamelower = fieldName.toLowerCase();
        fieldMap.put(fieldNamelower, editText.getText().toString().toLowerCase());
        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(fieldNamelower);
        editText.addTextChangedListener(setTextWhenChangedListener);
        BindingMethod method = new BindingMethod(){
            @Override
            public Object invoke(String fieldName, Object... args) {
                String value = (String) args[0];
                if(!value.equals(editText.getText().toString())) {
                    editText.removeTextChangedListener(setTextWhenChangedListener);
                    editText.setText(value);
                    editText.addTextChangedListener(setTextWhenChangedListener);
                }
                return null;
            }
        };
        String methodName = "set" + fieldName.toLowerCase();
        List<BindingMethod> methods = methodMap.get(methodName);
        if(methods == null){
            methods = new ArrayList<>();
            methodMap.put(methodName, methods);
        }
        methods.add(method);
    }

}
