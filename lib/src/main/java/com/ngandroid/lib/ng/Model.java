package com.ngandroid.lib.ng;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by davityle on 1/12/15.
 */
public class Model implements InvocationHandler {

    private final MethodInvoker invocationHandler;

    public Model(MethodInvoker invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return invocationHandler.invoke(method.getName(), objects);
    }
}
