package com.ngandroid.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ngandroid.lib.ngbind.BindingHandlerBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davityle on 1/12/15.
 */
public class NgAndroid {

    private final LayoutInflater mInflater;
    private final Activity activity;
    private final Object model;
    private final SparseArray<TypedArray> attrArray;
    private final Map<String, BindingHandlerBuilder> builders;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private NgAndroid(final Activity activity, Object model) {
        this.activity = activity;
        this.model = model;
        this.attrArray = new SparseArray<>();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.builders = new ArrayMap<>();
        }else {
            this.builders = new HashMap<>();
        }

        this.mInflater =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LayoutInflater.Factory factory = mInflater.getFactory();
        if(factory == null && Build.VERSION.SDK_INT >= 11){
            final LayoutInflater.Factory2 factory2 = mInflater.getFactory2();
            mInflater.setFactory2(new LayoutInflater.Factory2() {
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
            try {
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(mInflater, false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // TODO throw error
            }
            mInflater.setFactory(new LayoutInflater.Factory() {
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

    private void apply(View v){
        for(int index = 0; index < attrArray.size(); index++){
            int id = attrArray.keyAt(index);
            TypedArray array = attrArray.get(id);
            for(int i = 0 ; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case R.styleable.ngAndroid_ngModel:
                        TypedValue vl = new TypedValue();
                        array.getValue(R.styleable.ngAndroid_ngModel, vl);
                        String value= vl.string.toString();
                        if (value != null) {
                            int indexOfPeriod = value.indexOf('.');
                            String modelName = value.substring(0, indexOfPeriod);
                            final String fieldName = value.substring(indexOfPeriod + 1);

                            BindingHandlerBuilder builder = builders.get(modelName);

                            try {
                                if(builder == null){
                                    Field f = model.getClass().getDeclaredField(modelName);
                                    builder = new BindingHandlerBuilder(f.getType(), model);
                                    builders.put(modelName, builder);
                                }
                            } catch (NoSuchFieldException e) {
                                // TODO rename error
                                throw new RuntimeException("There is not a model in " + model.getClass().getSimpleName() + " called " + modelName);
                            }

                            View bindView = v.findViewById(id);

                            if (TextView.class.isAssignableFrom(bindView.getClass())) {
                                builder.addTextViewBind(fieldName, (TextView) bindView);
                            } else if (EditText.class.isAssignableFrom(bindView.getClass())) {
                                builder.addEditTextBind(fieldName, (EditText) bindView);
                            }
                        }
                        break;
                }

            }
        }

        for(Map.Entry<String, BindingHandlerBuilder> entry : builders.entrySet()){
            Object m = entry.getValue().create();
            String modelName = entry.getKey();
            try {
                Field f = model.getClass().getDeclaredField(modelName);
                f.setAccessible(true);
                f.set(model, m);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // TODO rename error
                throw new RuntimeException("There is not a model in " + model.getClass().getSimpleName() + " called " + modelName);
            }
        }
    }


    private void setContentView(int resourceId){
        View v = mInflater.inflate(resourceId, null);
        apply(v);
        activity.setContentView(v);
    }

    private View inflate(int resourceId, ViewGroup viewGroup, boolean attach){
        View v = mInflater.inflate(resourceId, viewGroup, attach);
        apply(v);
        return v;
    }


    public static void setContentView(Activity activity, int resourceId) {
        new NgAndroid(activity, activity).setContentView(resourceId);
    }

    public static void setContentView(Activity activity, int resourceId, Object model) {
        new NgAndroid(activity, model).setContentView(resourceId);
    }

    public static View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return new NgAndroid(activity, activity).inflate(resourceId, viewGroup, attach);
    }

    public static View inflate(Activity activity, Object model, int resourceId, ViewGroup viewGroup, boolean attach){
        return new NgAndroid(activity, model).inflate(resourceId, viewGroup, attach);
    }
}
