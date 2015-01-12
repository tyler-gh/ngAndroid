package com.ngandroid.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by davityle on 1/12/15.
 */
public class NgAndroid {

    private final LayoutInflater mInflater;
    private final Activity activity;
    private final SparseArray<TypedArray> attrArray;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private NgAndroid(final Activity activity) {
        this.activity = activity;
        this.attrArray = new SparseArray<>();
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
                        int id = Integer.parseInt(idValue.replace("@", ""));
                        attrArray.put(id, context.obtainStyledAttributes(attributeSet, R.styleable.ngAndroid));
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
            for(int i = 0 ; i < array.length(); i ++) {
                TypedValue value = new TypedValue();
                array.getValue(index, value);
                System.out.println(value.toString());
                System.out.println(value.coerceToString());

                if (value.string != null) {
                    System.out.println(value.string);
                    String dataBind = value.string.toString();
                    int indexOfPeriod = dataBind.indexOf('.');
                    String modelName = dataBind.substring(0, indexOfPeriod);
                    final String fieldName = dataBind.substring(indexOfPeriod + 1);

                    try {
                        Field f = activity.getClass().getDeclaredField(modelName);
                        f.setAccessible(true);
                        View bindView = v.findViewById(id);
                        if (bindView != null) {
                            if (TextView.class.isAssignableFrom(bindView.getClass())) {
                                final TextView textBind = (TextView) bindView;
                                Object model = java.lang.reflect.Proxy.newProxyInstance(f.getType().getClassLoader(), new java.lang.Class[] { f.getType() }, new java.lang.reflect.InvocationHandler() {
                                    @Override
                                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                        if(method.getName().toLowerCase().equals("set" + fieldName.toLowerCase())){
                                            textBind.setText((String)objects[0]);
                                        }
                                        return method.invoke(o,objects);
                                    }
                                });
                                f.set(activity, model);

                            } else if (EditText.class.isAssignableFrom(bindView.getClass())) {

                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
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
        new NgAndroid(activity).setContentView(resourceId);
    }

    public static View inflate(Activity activity, int resourceId, ViewGroup viewGroup, boolean attach){
        return new NgAndroid(activity).inflate(resourceId, viewGroup, attach);
    }
}
