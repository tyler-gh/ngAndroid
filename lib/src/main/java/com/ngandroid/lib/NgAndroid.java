package com.ngandroid.lib;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.lib.attacher.AttributeAttacher;

/**
 * Created by davityle on 1/12/15.
 */
public class NgAndroid {


    public static void setContentView(Activity activity, int resourceId) {
        new AttributeAttacher(activity, activity).setContentView(activity, resourceId);
    }

    public static void setContentView(Activity activity, int resourceId, Object model) {
        new AttributeAttacher(activity, model).setContentView(activity, resourceId);
    }

    public static View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return new AttributeAttacher(activity, activity).inflate(resourceId, viewGroup, attach);
    }

    public static View inflate(Activity activity, int resourceId, ViewGroup viewGroup){
        return new AttributeAttacher(activity, activity).inflate(resourceId, viewGroup, false);
    }

    public static View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach, Object model){
        return new AttributeAttacher(activity, model).inflate(resourceId, viewGroup, attach);
    }
}
