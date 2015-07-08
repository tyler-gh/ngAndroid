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
import com.ngandroid.lib.ng.ModelObserver;
import com.ngandroid.lib.ng.Scope;
import com.ngandroid.lib.ngattributes.AttrsResolver;
import com.ngandroid.lib.ngattributes.NgAttribute;
import com.ngandroid.lib.utils.DefaultValueFormatter;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.ValueFormatter;

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
     * @param scope the annotated scope object
     * @param activity contentView set
     * @param resourceId xml layout resource to bind to scope and set contentView of Activity
     */
    public void setContentView(Object scope, Activity activity, int resourceId) {
        setContentView(buildScope(scope), activity, resourceId);
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
     * this inflates the view using {@link Activity#getLayoutInflater()}, calls,
     * {@link #buildScope(Object)}, and then attaches the view to the scope using
     * {@link Scope#attach(int, View)}, the activity is used as the scope object
     * @param activity the scope and the context for the layout inflater
     * @param resourceId layout resource id
     * @param viewGroup  the parent of the view to be
     * @param attach attach to the view group
     * @return the inflated and attached view
     */
    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return inflate(buildScope(activity), activity, resourceId, viewGroup, attach);
    }

    /**
     * this inflates the view using {@link Activity#getLayoutInflater()}, calls,
     * {@link #buildScope(Object)}, and then attaches the view to the scope using
     * {@link Scope#attach(int, View)}, the activity is used as the scope object
     *
     * @param activity the scope and the context for the layout inflater
     * @param resourceId layout resource id
     * @param viewGroup the parent of the view to be, the view is not attched to the view group
     *                  by default
     * @return the inflated and attached view
     */
    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup){
        return inflate(buildScope(activity), activity, resourceId, viewGroup, false);
    }

    /**
     * this inflates the view using {@link Activity#getLayoutInflater()}, calls,
     * {@link #buildScope(Object)}, and then attaches the view to the scope using
     * {@link Scope#attach(int, View)}
     * @param scope the annotated scope object
     * @param activity activity is used to get the layout inflater
     * @param resourceId layout resource id
     * @param viewGroup attach to the view group
     * @param attach attach to the view group
     * @return the inflated and attached view
     */
    public View inflate(Object scope, Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        View v = activity.getLayoutInflater().inflate(resourceId, viewGroup, attach);
        buildScope(scope).attach(resourceId, v);
        return v;
    }

    /**
     * this inflates the view, calls,  {@link #buildScope(Object)}, and then attaches the view
     * to the scope using {@link Scope#attach(int, View)}
     * @param scope the annotated scope object
     * @param inflater inflater for inflating the view
     * @param resourceId layout resource id
     * @param viewGroup the parent of the view to be, the view is not attched to the view group
     *                  by default
     * @return the inflated and attached view
     */
    public View inflate(Object scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup){
        return inflate(scope, inflater, resourceId, viewGroup, false);
    }

    /**
     * programmatically add model field observer
     * @param scope the annotated scope object
     * @param modelName the name of the annotated {@link com.ngandroid.lib.annotations.NgModel} field
     * @param field the field within the model that will be observed
     * @param modelObserver the {@link ModelObserver}
     */
    public void observeModelField(Object scope, String modelName, String field, ModelObserver modelObserver){
        buildScope(scope).getModel(modelName).addObserver(field, modelObserver);
    }

    /**
     * this inflates the view, calls,  {@link #buildScope(Object)}, and then attaches the view
     * to the scope using {@link Scope#attach(int, View)}
     * @param scope the annotated scope object
     * @param inflater inflater for inflating the view
     * @param resourceId layout resource id
     * @param viewGroup the parent of the view to be
     * @param attach attach to the view group
     * @return the inflated and attached view
     */
    public View inflate(Object scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup, boolean attach){
        View v = inflater.inflate(resourceId, viewGroup, attach);
        buildScope(scope).attach(resourceId, v);
        return v;
    }

    /**
     * builds the scope, this function will set the fields annotated with
     * {@link com.ngandroid.lib.annotations.NgModel} that are null or do not
     * implement {@link com.ngandroid.lib.ng.Model}
     * @param scope the annotated scope object
     * @return the Scope
     */
    public Scope buildScope(Object scope) {
        return ScopeBuilder.getScope(scope, this);
    }

    /**
     * DO NOT USE. This is for generated code to access the NgAttributes
     */
    public void attach(int attr, Scope scope, View view, int layoutId, int viewId, Tuple<String,String> ... models) {
        NgAttribute ngAttribute = attributes.get(attr);
        if(ngAttribute == null)
            throw new NgException("Unable to find NgAttribute " + Integer.toHexString(attr));
        ngAttribute.attach(scope, view, layoutId, viewId, models);
    }

    /**
     * Customizes and Builds a NgAndroid
     */
    public static final class Builder {

        private ValueFormatter valueFormatter;

        /**
         * sets the value formatter that {@link com.ngandroid.lib.ngattributes.NgText} and
         * {@link com.ngandroid.lib.ngattributes.NgModel} will use to format and convert values
         * @param valueFormatter the value formatter
         * @return this
         */
        public Builder setValueFormatter(ValueFormatter valueFormatter){
            this.valueFormatter = valueFormatter;
            return this;
        }

        /**
         * builds the NgAndroid instance
         * @return NgAndroid
         */
        public NgAndroid build(){

            if(valueFormatter == null)
                valueFormatter = new DefaultValueFormatter();

            return new NgAndroid(AttrsResolver.getAttrsImpl().getAttributes(valueFormatter));
        }
    }
}
