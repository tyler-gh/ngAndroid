package com.ngandroid.lib.ngattributes.ngmodel;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by tyler on 4/23/15.
 */
// TODO this class needs an overhaul
public class TextInteracter implements TextWatcher, ModelMethod {

    private final Model model;
    private final String field;
    private final TextView view;
    private final int type;
    private String text = "";

    public TextInteracter(Model model, String field, TextView view) {
        this.model = model;
        this.field = field;
        this.view = view;
        this.type = TypeUtils.getType(model.getType(field));
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable editable) {
        Object value;
        String str;
        try {
            str = editable.toString();
            value = TypeUtils.fromStringEmptyStringIfEmpty(type, str);
        } catch (Throwable e) {
            Log.w("TextInteracter", "invalid input");
            view.removeTextChangedListener(this);
            editable.clear();
            editable.append(text);
            view.addTextChangedListener(this);
            str = text;
            value = TypeUtils.fromStringEmptyStringIfEmpty(type, str);
        }

        text = str;
        try{
            model.setValue(field, value);
        } catch (Throwable e) {
            Log.e("TextInteracter", "Setting the value of " + model + "." + field + " produced an error.", e);
        }
    }

    @Override
    public Object invoke(String fieldName, Object... args) {
        String value = String.valueOf(args[0]);
        if (!value.equals(view.getText().toString())) {
            view.removeTextChangedListener(this);
            int position = view.getSelectionStart();
            view.setText(value.equals("0") || value.equals("0.0") ? "" : value);
            if(view instanceof EditText){
                int length = view.getText().length();
                if(length != 0)
                    ((EditText) view).setSelection(position < length ? position : length -1);
            }
            view.addTextChangedListener(this);
        }
        return null;
    }
}
