package com.ngandroid.lib.ngbind;

/**
 * Created by davityle on 1/12/15.
 */
interface BindingMethod {
    public Object invoke(String fieldName, Object ... args);
}
