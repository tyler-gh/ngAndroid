package com.ngandroid.lib.ngattributes;

import android.util.SparseArray;

import com.ngandroid.lib.R;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.utils.ValueFormatter;

/**
 * Created by tyler on 4/24/15.
 */
public class Attrs {

    public static SparseArray<NgAttribute> getAttributes(ValueFormatter formatter){
        SparseArray<NgAttribute> attributes = new SparseArray<>();
        attributes.put(R.styleable.ngAndroid_ngModel, NgModel.getInstance(formatter));
        attributes.put(R.styleable.ngAndroid_ngClick, NgClick.getInstance());
        attributes.put(R.styleable.ngAndroid_ngLongClick, NgLongClick.getInstance());
        attributes.put(R.styleable.ngAndroid_ngChange, NgChange.getInstance());
        attributes.put(R.styleable.ngAndroid_ngGone, NgGone.getInstance());
        attributes.put(R.styleable.ngAndroid_ngInvisible, NgInvisible.getInstance());
        attributes.put(R.styleable.ngAndroid_ngDisabled, NgDisabled.getInstance());
        attributes.put(R.styleable.ngAndroid_ngBlur, NgBlur.getInstance());
        attributes.put(R.styleable.ngAndroid_ngFocus, NgFocus.getInstance());
        attributes.put(R.styleable.ngAndroid_ngText, NgText.getInstance(formatter));
        return attributes;
    }

}
