package com.ngandroid.lib.attacher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngandroid.lib.R;
import com.ngandroid.lib.ngbind.BindingHandlerBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davityle on 1/13/15.
 */
public class AttributeAttacher {

    private final LayoutInflater mInflater;
    private final Object model;
    private final SparseArray<TypedArray> attrArray;
    private final Map<String, BindingHandlerBuilder> builders;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public AttributeAttacher(final Context context, Object model) {
        this.model = model;
        this.attrArray = new SparseArray<>();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.builders = new ArrayMap<>();
        }else {
            this.builders = new HashMap<>();
        }

        this.mInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        InflaterFactory.setFactory(mInflater, attrArray);
    }

    private void apply(View v){
        System.out.println("/////////////?????????????////////////");
        System.out.println(attrArray.size());
        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        for(int index = 0; index < attrArray.size(); index++){
            int id = attrArray.keyAt(index);
            TypedArray array = attrArray.get(id);
            for(int i = 0 ; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case R.styleable.ngAndroid_ngModel:
                        TypedValue vl = new TypedValue();
                        System.out.println("/////////////////////////////////////");
                        System.out.println(attr);
                        System.out.println(R.styleable.ngAndroid_ngModel);
                        System.out.println(array);
                        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
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
