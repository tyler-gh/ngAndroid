
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

package com.ngandroid.lib.attacher;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.ngandroid.lib.R;

import java.lang.reflect.Field;

/**
 * Created by davityle on 1/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InflaterFactory implements LayoutInflater.Factory2, LayoutInflater.Factory {

    private final LayoutInflater.Factory2 mFactory2;
    private final LayoutInflater.Factory mFactory;
    private SparseArray<TypedArray> mAttrArray;

    private InflaterFactory(LayoutInflater.Factory factory, SparseArray<TypedArray> attrArray) {
        this(null, factory, attrArray);
    }

    private InflaterFactory(LayoutInflater.Factory2 factory2, SparseArray<TypedArray> attrArray) {
        this(factory2, null, attrArray);
    }

    private InflaterFactory(LayoutInflater.Factory2 factory2, LayoutInflater.Factory factory, SparseArray<TypedArray> attrArray) {
        this.mFactory2 = factory2;
        this.mFactory = factory;
        this.mAttrArray = attrArray;
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        if(mFactory != null){
            return mFactory.onCreateView(s, context, attributeSet);
        }
        if(mFactory2 == null){
            return null;
        }
        return mFactory2.onCreateView(s, context, attributeSet);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        if(mFactory2 == null){
            return null;
        }
        return mFactory2.onCreateView(view,s,context, attributeSet);
    }

    private void parseAttributes(Context context, AttributeSet attributeSet){
        String idValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
        if(idValue != null) {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ngAndroid);
            if(array.getIndexCount() > 0) {
                int id = Integer.parseInt(idValue.replace("@", ""));
                mAttrArray.put(id, array);
            }
        }
    }

    private static void setSettable(LayoutInflater inflater){
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // TODO throw error
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static void setFactory(LayoutInflater inflater,final  SparseArray<TypedArray> attrArray){
        final LayoutInflater.Factory factory = inflater.getFactory();
        if(factory instanceof InflaterFactory){
            InflaterFactory inflaterFactory = (InflaterFactory) factory;
            inflaterFactory.mAttrArray = attrArray;
        }else if(factory == null && Build.VERSION.SDK_INT >= 11){
            final LayoutInflater.Factory2 factory2 = inflater.getFactory2();
            inflater.setFactory2(new InflaterFactory(factory2, attrArray));
        }else if(factory != null){
            setSettable(inflater);
            inflater.setFactory(new InflaterFactory(factory, attrArray));
        }else{
            throw new RuntimeException();
            // TODO throw error
        }

    }
}
