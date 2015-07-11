package com.ngandroid.lib.ngattributes;

import android.util.SparseArray;

import com.ngandroid.lib.utils.Blur;
import com.ngandroid.lib.utils.ValueFormatter;

/**
 * Created by tyler on 4/24/15.
 */
public interface Attrs {
    SparseArray<NgAttribute> getAttributes(ValueFormatter formatter, Blur blur);
}
