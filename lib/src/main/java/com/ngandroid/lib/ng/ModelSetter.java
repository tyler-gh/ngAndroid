package com.ngandroid.lib.ng;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelSetter implements Setter {

    private final String mFieldName;
    private final MethodInvoker mMethodInvoker;

    public ModelSetter(String mFieldName, MethodInvoker mMethodInvoker) {
        this.mFieldName = mFieldName;
        this.mMethodInvoker = mMethodInvoker;
    }

    public void set(Object ... parameters) throws Throwable {
        mMethodInvoker.invoke("set" + mFieldName, parameters);
    }
}
