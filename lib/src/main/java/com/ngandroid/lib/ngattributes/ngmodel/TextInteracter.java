package com.ngandroid.lib.ngattributes.ngmodel;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by tyler on 4/23/15.
 */
public class TextInteracter implements TextWatcher, ModelMethod {

    private final Model model;
    private final String field;
    private final TextView view;
    private final int type;
    private String text;

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
            // TODO handle error | this replace does not actually work
            editable.replace(0, editable.length(), text);
            e.printStackTrace();
            return;
        }
        text = str;
        try{
            model.setValue(field, value);
//            mSetter.set(value);
        } catch (Throwable e) {
            // TODO handle error
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(String fieldName, Object... args) {
        String value = String.valueOf(args[0]);
        if (!value.equals(view.getText().toString())) {
            view.removeTextChangedListener(this);
            view.setText(value.equals("0") ? "" : value);
            view.addTextChangedListener(this);
        }
        return null;
    }
}
