package com.ngandroid.lib.ngattributes.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelObserver;
import com.ngandroid.lib.utils.TypeUtils;
import com.ngandroid.lib.utils.ValueFormatter;

/**
 * Created by tyler on 4/23/15.
 */
// TODO this class needs an overhaul
public class TextInteracter implements TextWatcher, ModelObserver {

    private static final String TAG = "TextInteracter";

    private final Model model;
    private final String field;
    private final TextView view;
    private final ValueFormatter valueFormatter;
    private final Class<?> type;
    private final int intType;
    private boolean fromSelf;
    private String text = "";

    public TextInteracter(Model model, String field, TextView view, ValueFormatter valueFormatter) {
        this.model = model;
        this.field = field;
        this.view = view;
        this.valueFormatter = valueFormatter;
        this.type = model.getType(field);
        this.intType = TypeUtils.getType(type);
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable editable) {
        Object value;
        String str;
        try {
            str = editable.toString();
            value = valueFormatter.toValue(intType, type, str, text);
        } catch (Throwable e) {
            Log.w(TAG, "invalid input value is staying the same");
            return;
        }

        text = str;
        fromSelf = true;
        try {
            model.setValue(field, value);
        } catch (Throwable e) {
            Log.e(TAG, "Setting the value of " + model + "." + field + " produced an error.", e);
        }
        fromSelf = false;
    }

    @Override
    public void invoke(String fieldName, Object arg) {
        if(fromSelf)
            return;
        String value = valueFormatter.format(arg);
        String current = view.getText().toString();
        if (!value.equals(current)) {
            int position = view.getSelectionStart();
            String filtered;
            try {
                filtered = valueFormatter.filter(value, current);
            }catch (Exception e){
                Log.e(TAG, "ValueFormatter.filter threw an error" ,e);
                filtered = value;
            }
            view.removeTextChangedListener(this);
            view.setText(filtered);
            view.addTextChangedListener(this);
            if (view instanceof EditText) {
                int length = view.getText().length();
                if (length != 0)
                    ((EditText) view).setSelection(position < length ? position : length - 1);
            }
        }
    }
}
