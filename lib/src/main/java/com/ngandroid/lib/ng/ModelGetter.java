package com.ngandroid.lib.ng;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelGetter implements Getter {

    private final String mFieldName;
    private final MethodInvoker mMethodInvoker;

    public ModelGetter(String mFieldName, MethodInvoker mMethodInvoker) {
        this.mFieldName = mFieldName;
        this.mMethodInvoker = mMethodInvoker;
    }

    public Object get() throws Throwable {
        return mMethodInvoker.invoke("get" + mFieldName);
    }
}
