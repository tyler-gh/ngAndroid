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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.lib.attacher.AttributeAttacher;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davityle on 1/12/15.
 */
public class NgAndroid {

    private static NgAndroid instance;

    /**
     * this method is not thread safe
     * @return
     */
    public static NgAndroid getInstance(){
        if(instance == null){
            instance = new Builder().build();
        }
        return instance;
    }

    private final SparseArray<NgAttribute> mCustomAttributes;

    private NgAndroid(SparseArray<NgAttribute> customAttributes){
        this.mCustomAttributes = customAttributes;
    }


    public void setContentView(Activity activity, int resourceId) {
        setContentView(activity, activity, resourceId);
    }

    public void setContentView(Object scope, Activity activity, int resourceId) {
        new AttributeAttacher(activity, scope, mCustomAttributes).setContentView(activity, resourceId);
    }

    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return inflate(activity, activity, resourceId, viewGroup, attach);
    }

    public View inflate(Activity activity, int resourceId, ViewGroup viewGroup){
        return inflate(activity, activity, resourceId, viewGroup, false);
    }

    public View inflate(Object scope, Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return new AttributeAttacher(activity, scope, mCustomAttributes).inflate(resourceId, viewGroup, attach);
    }

    public View inflate(Object scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup){
        return inflate(scope, inflater, resourceId, viewGroup, false);
    }

    public View inflate(Object scope, LayoutInflater inflater, int resourceId, ViewGroup viewGroup, boolean attach){
        return new AttributeAttacher(inflater, scope, mCustomAttributes).inflate(resourceId, viewGroup, attach);
    }

    public <T> T buildModel(Class<T> clss){
        return (T) new ModelBuilder(clss).create();
    }

    public <T> T modelFromJson(String json, Class<T> clss) throws JSONException {
        return JsonUtils.buildModelFromJson(new JSONObject(json), clss);
    }


    public static final class Builder {
        private SparseArray<NgAttribute> mCustomAttributes;

        public Builder addCustomAttribute(int attributeId, NgAttribute ngAttribute){
            if(mCustomAttributes == null){
                mCustomAttributes = new SparseArray<>();
            }
            mCustomAttributes.append(attributeId, ngAttribute);
            return this;
        }

        public NgAndroid build(){
            return new NgAndroid(mCustomAttributes);
        }
    }
}
