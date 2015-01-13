package com.ngandroid.lib.ngbind;

/**
 * Created by davityle on 1/12/15.
 */
public interface Invoker {
    public Object invoke(Object o, String methodName, Object[] objects) throws Throwable;
}
