package com.ngandroid.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by davityle on 1/12/15.
 */
public class InflaterFactory implements LayoutInflater.Factory2, LayoutInflater.Factory {

    private final LayoutInflater.Factory2 factory2;
    private final LayoutInflater.Factory factory;
    private final SparseArray<TypedArray> attrArray;

    private InflaterFactory(LayoutInflater.Factory factory, SparseArray<TypedArray> attrArray) {
        this(null, factory, attrArray);
    }

    private InflaterFactory(LayoutInflater.Factory2 factory2, SparseArray<TypedArray> attrArray) {
        this(factory2, null, attrArray);
    }

    private InflaterFactory(LayoutInflater.Factory2 factory2, LayoutInflater.Factory factory, SparseArray<TypedArray> attrArray) {
        this.factory2 = factory2;
        this.factory = factory;
        this.attrArray = attrArray;
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        if(factory != null){
            return factory.onCreateView(s, context, attributeSet);
        }
        return factory2.onCreateView(s, context, attributeSet);
    }

    public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        return factory2.onCreateView(view,s,context, attributeSet);
    }

    private void parseAttributes(Context context, AttributeSet attributeSet){
        String idValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
        if(idValue != null) {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ngAndroid);
            if(array.getIndexCount() > 0) {
                int id = Integer.parseInt(idValue.replace("@", ""));
                attrArray.put(id, array);
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

    static void setFactory(LayoutInflater inflater,final  SparseArray<TypedArray> attrArray){
        final LayoutInflater.Factory factory = inflater.getFactory();
        if(factory == null && Build.VERSION.SDK_INT >= 11){
            final LayoutInflater.Factory2 factory2 = inflater.getFactory2();
            inflater.setFactory2(new LayoutInflater.Factory2() {
                @Override
                public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
                    return factory2.onCreateView(view,s,context, attributeSet);
                }

                @Override
                public View onCreateView(String s, Context context, AttributeSet attributeSet) {
                    return factory2.onCreateView(s, context, attributeSet);
                }
            });
        }else if(factory != null){
            setSettable(inflater);
            inflater.setFactory(new LayoutInflater.Factory() {
                @Override
                public View onCreateView(String s, Context context, AttributeSet attributeSet) {
                    View v = factory.onCreateView(s, context, attributeSet);
                    String idValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
                    if(idValue != null) {
                        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ngAndroid);
                        if(array.getIndexCount() > 0) {
                            int id = Integer.parseInt(idValue.replace("@", ""));
                            attrArray.put(id, array);
                        }
                    }
                    return v;
                }
            });
        }else{
            // TODO throw error
        }

    }
}
