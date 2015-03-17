
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

import com.ngandroid.lib.interpreter.ExpressionBuilder;
import com.ngandroid.lib.interpreter.SyntaxParser;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.ModelGetter;

/**
 * Created by davityle on 1/13/15.
 */
public class AttributeAttacher {

    private LayoutInflater mInflater;
    private Object mScope;
    private SparseArray<TypedArray> mAttrArray;
    private ModelBuilderMap mBuilders;
    private SparseArray<NgAttribute> attributes;

    public AttributeAttacher(final Context context, Object scope, SparseArray<NgAttribute> customAttributes) {
        this((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), scope, customAttributes);
    }

    public AttributeAttacher(final LayoutInflater inflater, Object scope, SparseArray<NgAttribute> attributes) {
        this.mScope = scope;
        this.mAttrArray = new SparseArray<>();
        this.mBuilders = new ModelBuilderMap(mScope);
        this.mInflater = inflater;
        this.attributes = attributes;
        InflaterFactory.setFactory(mInflater, mAttrArray);
    }

    private void apply(View v){
        for(int index = 0; index < mAttrArray.size(); index++){
            int id = mAttrArray.keyAt(index);
            TypedArray array = mAttrArray.get(id);
            for(int i = 0 ; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                String value = array.getString(attr);
                Token[] tokens = new SyntaxParser(value).parseScript();
                Getter getter = new ExpressionBuilder(tokens).build(mScope, mBuilders);

                NgAttribute attribute = attributes.get(attr);
                if(attribute == null)
                    throw new UnsupportedOperationException("Attribute not currently implemented");
                ModelGetter[] modelGetters = ModelGetter.getModelGetters(getter);
                ModelBuilder[] modelBuilders = ModelGetter.getModelBuilders(modelGetters, mBuilders);
                try {
                    attribute.typeCheck(tokens, getter);
                    attribute.attach(getter, modelGetters, modelBuilders, v.findViewById(id));
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
        kill();
    }

    public View inflate(int resourceId, ViewGroup viewGroup, boolean attach){
        View v = mInflater.inflate(resourceId, viewGroup, attach);
        apply(v);
        kill();
        return v;
    }

    private void kill(){
        mInflater = null;
        mScope = null;
        mBuilders = null;
        mAttrArray = null;
        attributes = null;
    }
}
