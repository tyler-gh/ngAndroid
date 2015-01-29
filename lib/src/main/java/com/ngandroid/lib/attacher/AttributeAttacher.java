package com.ngandroid.lib.attacher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.lib.R;
import com.ngandroid.lib.interpreter.SyntaxParser;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ngClick.NgClick;
import com.ngandroid.lib.ngmodel.NgModel;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by davityle on 1/13/15.
 */
public class AttributeAttacher {

    private final LayoutInflater mInflater;
    private final Object mModel;
    private final SparseArray<TypedArray> mAttrArray;
    private final ModelBuilderMap mBuilders;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public AttributeAttacher(final Context context, Object model) {
        this.mModel = model;
        this.mAttrArray = new SparseArray<>();
        this.mBuilders = new ModelBuilderMap(mModel);
        this.mInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        InflaterFactory.setFactory(mInflater, mAttrArray);
    }

    private void apply(View v){
        for(int index = 0; index < mAttrArray.size(); index++){
            int id = mAttrArray.keyAt(index);
            TypedArray array = mAttrArray.get(id);
            for(int i = 0 ; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                Token[] tokens = new SyntaxParser(array.getString(attr)).parseScript();
                if(attr == R.styleable.ngAndroid_ngModel){
                    try {
                        NgModel.getInstance().attach(tokens, mModel, mBuilders, v.findViewById(id));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else if (attr == R.styleable.ngAndroid_ngClick){
                    NgClick.getInstance().attach(tokens, mModel, mBuilders, v.findViewById(id));
                }
            }
        }

        for(Map.Entry<String, ModelBuilder> entry : mBuilders.entrySet()){
            attachDynamicField(entry.getValue().create(), entry.getKey());
        }
    }

    private void attachDynamicField(Object dynamicField, String modelName){
        try {
            Field f = mModel.getClass().getDeclaredField(modelName);
            f.setAccessible(true);
            f.set(mModel, dynamicField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // TODO rename error
            throw new RuntimeException("There is not a field in " + mModel.getClass().getSimpleName() + " called " + modelName);
        }
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
