package com.ngandroid.lib.ngattributes;

import com.ngandroid.lib.exceptions.NgException;

/**
 * Created by tyler on 7/7/15.
 */
public class AttrsResolver {

    public static Attrs getAttrsImpl(){
        try {
            return ((Class<Attrs>) Class.forName("com.ngandroid.lib.ngattributes.AttrsImpl")).newInstance();
        } catch (Exception e) {
            throw new NgException("You must include apt 'com.github.davityle:ng-processor:##' in your gradle dependencies", e);
        }
    }
}
