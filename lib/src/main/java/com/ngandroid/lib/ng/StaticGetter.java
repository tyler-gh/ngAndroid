package com.ngandroid.lib.ng;

/**
 * Created by davityle on 1/24/15.
 */
public class StaticGetter implements Getter{
    private final Object object;

    public StaticGetter(Object object) {
        this.object = object;
    }

    @Override
    public Object get() throws Throwable {
        return object;
    }
}
