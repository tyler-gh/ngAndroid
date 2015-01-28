package com.ngandroid.lib.ngClick;

import android.view.View;

import com.ngandroid.lib.ng.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* Created by davityle on 1/24/15.
*/
class ClickInvoker implements View.OnClickListener {

    private final Method mMethod;
    private final Object mModel;
    private final Getter[] mGetters;

    ClickInvoker(Method method, Object model, Getter... getters) {
        this.mMethod = method;
        this.mModel = model;
        this.mGetters = getters;
        mMethod.setAccessible(true);
    }

    @Override
    public void onClick(View view) {
        Object[] parameters = new Object[mGetters.length];
        for(int index = 0; index < parameters.length; index++){
            try {
                parameters[index] = mGetters[index].get();
            } catch (Throwable throwable) {
                // TODO error
                throwable.printStackTrace();
            }
        }
        try {
            mMethod.invoke(mModel, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO error
            e.printStackTrace();
        }
    }
}
