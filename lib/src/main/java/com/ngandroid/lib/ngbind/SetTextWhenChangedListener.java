package com.ngandroid.lib.ngbind;

import android.text.Editable;
import android.text.TextWatcher;

import com.ngandroid.lib.utils.TypeUtils;

/**
* Created by davityle on 1/12/15.
*/
class SetTextWhenChangedListener implements TextWatcher {
    private final String mFieldName;
    private final MethodInvoker mInvocationHandler;
    private final Object mModel;
    private final int mMethodType;
    private String mValidText = "";

    SetTextWhenChangedListener(String fieldName, MethodInvoker invocationHandler, Object model, int mMethodType) {
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
            value = TypeUtils.fromString(mMethodType, str);
        } catch (Throwable e) {
            // TODO handle error
            editable.replace(0, editable.length(), mValidText);
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
