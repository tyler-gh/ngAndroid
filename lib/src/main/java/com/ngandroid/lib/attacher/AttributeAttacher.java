
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

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.lib.R;
import com.ngandroid.lib.interpreter.ExpressionBuilder;
import com.ngandroid.lib.interpreter.SyntaxParser;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ngattributes.ngchange.NgChange;
import com.ngandroid.lib.ngattributes.ngclick.NgClick;
import com.ngandroid.lib.ngattributes.ngif.NgDisabled;
import com.ngandroid.lib.ngattributes.ngif.NgGone;
import com.ngandroid.lib.ngattributes.ngif.NgInvisible;
import com.ngandroid.lib.ngattributes.nglongclick.NgLongClick;
import com.ngandroid.lib.ngattributes.ngmodel.NgModel;

/**
 * Created by davityle on 1/13/15.
 */
public class AttributeAttacher {

    private final LayoutInflater mInflater;
    private final Object mScope;
    private final SparseArray<TypedArray> mAttrArray;
    private final ModelBuilderMap mBuilders;

    public AttributeAttacher(final Context context, Object model) {
        this((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), model);
    }

    public AttributeAttacher(final LayoutInflater inflater, Object scope) {
        this.mScope = scope;
        this.mAttrArray = new SparseArray<>();
        this.mBuilders = new ModelBuilderMap(mScope);
        this.mInflater = inflater;
        InflaterFactory.setFactory(mInflater, mAttrArray);
    }

    private void apply(View v){
        for(int index = 0; index < mAttrArray.size(); index++){
            int id = mAttrArray.keyAt(index);
            TypedArray array = mAttrArray.get(id);
            for(int i = 0 ; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                Token[] tokens = new SyntaxParser(array.getString(attr)).parseScript();
                Getter getter = new ExpressionBuilder(tokens).build(mScope, mBuilders);

                NgAttribute attribute;
                if(attr == R.styleable.ngAndroid_ngModel){
                    attribute = NgModel.getInstance();
                }else if (attr == R.styleable.ngAndroid_ngClick){
                    attribute = NgClick.getInstance();
                }else if(attr == R.styleable.ngAndroid_ngLongClick){
                    attribute = NgLongClick.getInstance();
                }else if(attr == R.styleable.ngAndroid_ngChange){
                    attribute = NgChange.getInstance();
                }else if(attr == R.styleable.ngAndroid_ngGone){
                    attribute = NgGone.getInstance();
                }else if(attr == R.styleable.ngAndroid_ngInvisible){
                    attribute = NgInvisible.getInstance();
                }else if(attr == R.styleable.ngAndroid_ngDisabled){
                    attribute = NgDisabled.getInstance();
                }else {
                    throw new UnsupportedOperationException("Attribute not currently implemented");
                }
                try {
                    attribute.typeCheck(tokens, getter);
                    attribute.attach(getter, mBuilders, v.findViewById(id));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ModelBuilder.buildModel(mScope, mBuilders);
    }
    public void setContentView(Activity activity, int resourceId){
        View v = mInflater.inflate(resourceId, null);
        apply(v);
        activity.setContentView(v);
    }

    public View inflate(int resourceId, ViewGroup viewGroup, boolean attach){
        View v = mInflater.inflate(resourceId, viewGroup, attach);
        apply(v);
        return v;
    }
}
