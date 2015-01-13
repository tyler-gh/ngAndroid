package com.ngandroid.lib.ngbind;

import android.text.Editable;
import android.text.TextWatcher;

/**
* Created by davityle on 1/12/15.
*/
class SetTextWhenChangedListener implements TextWatcher {
    private final String fieldName;
    private final MethodInvoker mInvocationHandler;
    private final Object mModel;

    SetTextWhenChangedListener(String fieldName, MethodInvoker invocationHandler, Object mModel) {
        this.fieldName = fieldName;
        this.mInvocationHandler = invocationHandler;
        this.mModel = mModel;
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
    @Override
    public void afterTextChanged(Editable editable) {
        try {
            mInvocationHandler.invoke(mModel, "set" + fieldName, new Object[]{editable.toString()});
        } catch (Throwable throwable) {
            // TODO handle error
            throwable.printStackTrace();
        }
    }
}
