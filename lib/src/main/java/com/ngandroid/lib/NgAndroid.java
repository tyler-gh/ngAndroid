/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ngandroid.lib;

import android.app.Activity;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.lib.exceptions.NgException;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.ng.ScopeBuilder;
import com.ngandroid.lib.ngattributes.ngblur.NgBlur;
import com.ngandroid.lib.ngattributes.ngchange.NgChange;
import com.ngandroid.lib.ngattributes.ngclick.NgClick;
import com.ngandroid.lib.ngattributes.ngfocus.NgFocus;
import com.ngandroid.lib.ngattributes.ngif.NgDisabled;
import com.ngandroid.lib.ngattributes.ngif.NgGone;
import com.ngandroid.lib.ngattributes.ngif.NgInvisible;
import com.ngandroid.lib.ngattributes.nglongclick.NgLongClick;
import com.ngandroid.lib.ngattributes.ngmodel.NgModel;
import com.ngandroid.lib.utils.Tuple;

/**
 * Created by davityle on 1/12/15.
 */
public class NgAndroid {

    private static NgAndroid instance;

    /**
     * returns a default singleton instance of NgAndroid
     * @return NgAndroid
     *
     * @throws NgException if not called on the main ui thread
     */
    public static NgAndroid getInstance(){
        if(Looper.myLooper() != Looper.getMainLooper())
            throw new NgException("NgAndroid should only be used on the main ui thread.");
        if(instance == null){
            instance = new Builder().build();
        }
        return instance;
    }

    private final SparseArray<NgAttribute> attributes;

    /**
     * private constructor
     * @param attributes
     */
    private NgAndroid(SparseArray<NgAttribute> attributes){
        this.attributes = attributes;
    }

    /**
     * sets the content view of the Activity to the given resourceId
     * @param activity used as the scope of the binding, contentView set
     * @param resourceId xml layout resource to bind to scope and set contentView of Activity
     */
    public void setContentView(Activity activity, int resourceId) {
        setContentView(buildScope(activity), activity, resourceId);
    }

    /**
     * sets the content view of the Activity to the given resourceId
     * binds the scope to the view
     * @param scope scope of the binding
     * @param activity contentView set
     * @param resourceId xml layout resource to bind to scope and set contentView of Activity
     */
    public void setContentView(Scope scope, Activity activity, int resourceId) {
        activity.setContentView(resourceId);
        scope.attach(resourceId, activity.findViewById(android.R.id.content));
    }

    /**
     * inflates and binds a view
     * @param activity
     * @param resourceId
     * @param viewGroup
     * @param attach
     * @return
     */
    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return inflate(buildScope(activity), activity, resourceId, viewGroup, attach);
    }

    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup){
        return inflate(buildScope(activity), activity, resourceId, viewGroup, false);
    }

    public View inflate(Scope scope, Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        View v = activity.getLayoutInflater().inflate(resourceId, viewGroup, attach);
        scope.attach(resourceId, v);
        return v;
    }

    public View inflate(Scope scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup){
        return inflate(scope, inflater, resourceId, viewGroup, false);
    }

    public View inflate(Scope scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup, boolean attach){
        View v = inflater.inflate(resourceId, viewGroup, attach);
        scope.attach(resourceId, v);
        return v;
    }

    public Scope buildScope(Object scope) {
        return ScopeBuilder.getScope(scope, this);
    }

    /**
     * DO NOT USE. This is for generated code to access the NgAttributes
     */
    public void attach(int attr, Scope scope, View view, int layoutId, int viewId, Tuple<String,String> ... models) {
        attributes.get(attr).attach(scope, view, layoutId, viewId, models);
    }

    public static final class Builder {
        private SparseArray<NgAttribute> attributes = new SparseArray<>();

        public NgAndroid build(){
            attributes.put(R.styleable.ngAndroid_ngModel, NgModel.getInstance());
            attributes.put(R.styleable.ngAndroid_ngClick, NgClick.getInstance());
            attributes.put(R.styleable.ngAndroid_ngLongClick, NgLongClick.getInstance());
            attributes.put(R.styleable.ngAndroid_ngChange, NgChange.getInstance());
            attributes.put(R.styleable.ngAndroid_ngGone, NgGone.getInstance());
            attributes.put(R.styleable.ngAndroid_ngInvisible, NgInvisible.getInstance());
            attributes.put(R.styleable.ngAndroid_ngDisabled, NgDisabled.getInstance());
            attributes.put(R.styleable.ngAndroid_ngBlur, NgBlur.getInstance());
            attributes.put(R.styleable.ngAndroid_ngFocus, NgFocus.getInstance());
            return new NgAndroid(attributes);
        }
    }
}
