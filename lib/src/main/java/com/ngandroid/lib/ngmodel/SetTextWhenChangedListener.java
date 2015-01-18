package com.ngandroid.lib.ngmodel;

import android.text.Editable;
import android.text.TextWatcher;

import com.ngandroid.lib.ng.MethodInvoker;
import com.ngandroid.lib.utils.TypeUtils;

/**
* Created by davityle on 1/12/15.
*/
public class SetTextWhenChangedListener implements TextWatcher {
    private final String mFieldName;
    private final MethodInvoker mInvocationHandler;
    private final Object mModel;
    private final int mMethodType;
    private String mValidText = "";

    public SetTextWhenChangedListener(String fieldName, MethodInvoker invocationHandler, Object model, int mMethodType) {
        this.mFieldName = fieldName;
        this.mInvocationHandler = invocationHandler;
        this.mModel = model;
        this.mMethodType = mMethodType;
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override
    public void afterTextChanged(Editable editable) {
        Object value;
        String str;
        try {
            str = editable.toString();
            value = TypeUtils.fromStringEmptyStringIfEmpty(mMethodType, str);
        } catch (Throwable e) {
            // TODO handle error | this replace does not actually work
            editable.replace(0, editable.length(), mValidText);
            e.printStackTrace();
            return;
        }
        mValidText = str;
        try{
            mInvocationHandler.invoke(mModel, "set" + mFieldName, new Object[]{value});
        } catch (Throwable e) {
            // TODO handle error
            e.printStackTrace();
        }
    }
}
