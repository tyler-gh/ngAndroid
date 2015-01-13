package com.ngandroid.lib.ngbind;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by davityle on 1/12/15.
 */
public class BindingHandler implements InvocationHandler {

    private final MethodInvoker invocationHandler;

    public BindingHandler(MethodInvoker invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return invocationHandler.invoke(o, method.getName(), objects);
    }
}
