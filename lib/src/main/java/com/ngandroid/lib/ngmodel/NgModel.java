package com.ngandroid.lib.ngmodel;

import android.view.View;
import android.widget.TextView;

import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ng.MethodInvoker;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by davityle on 1/17/15.
 */
public class NgModel {
    private static NgModel ourInstance = new NgModel();

    public static NgModel getInstance() {
        return ourInstance;
    }

    private NgModel() {}

    public void typeCheck(Token[] tokens){
        if(tokens.length != 4){
            // TODO handle error
            throw new RuntimeException("Invalid model syntax");
        }
        assertEquals(tokens[0].getTokenType(), TokenType.MODEL_NAME);
        assertEquals(tokens[1].getTokenType(), TokenType.PERIOD);
        assertEquals(tokens[2].getTokenType(), TokenType.MODEL_FIELD);
        assertEquals(tokens[3].getTokenType(), TokenType.EOF);
    }

    private void assertEquals(TokenType actual, TokenType expected){
        if(!actual.equals(expected)){
            throw new RuntimeException("Invalid model syntax\nExpected " + expected + " instead of " + actual);
        }
    }

    public void attach(final Token[] tokens, ModelBuilderMap builders, View bindView){
        typeCheck(tokens);
        String modelName = tokens[0].getScript();
        String fieldName = tokens[2].getScript();
        ModelBuilder builder = builders.get(modelName);
        if (TextView.class.isAssignableFrom(bindView.getClass())) {
            builder.ngModelBindTextView(this, fieldName, (TextView) bindView);
        }
    }

    public void bindModelToTextView(String fieldName, final TextView textView, Map<String, Object> mFieldMap, Map<String, List<ModelMethod>> mMethodMap, Method[] mModelMethods, MethodInvoker mInvocationHandler, Object mModel){
        final String fieldNamelower = fieldName.toLowerCase();
        String defaultText =  textView.getText().toString();
        mFieldMap.put(fieldNamelower, defaultText);
        int methodType = TypeUtils.STRING;
        for(Method m : mModelMethods){
            if(m.getName().toLowerCase().equals("set" + fieldNamelower)){
                methodType = TypeUtils.getType(m.getParameterTypes()[0]);
                break;
            }
        }
        final SetTextWhenChangedListener setTextWhenChangedListener = new SetTextWhenChangedListener(fieldNamelower, mInvocationHandler, mModel, methodType);
        textView.addTextChangedListener(setTextWhenChangedListener);
        ModelMethod method = new ModelMethod(){
            @Override
            public Object invoke(String fieldName, Object... args) {
                String value = String.valueOf(args[0]);
                if(!value.equals(textView.getText().toString())) {
                    textView.removeTextChangedListener(setTextWhenChangedListener);
                    textView.setText(value);
                    textView.addTextChangedListener(setTextWhenChangedListener);
                }
                return null;
            }
        };
        String methodName = "set" + fieldName.toLowerCase();
        List<ModelMethod> methods = mMethodMap.get(methodName);
        if(methods == null){
            methods = new ArrayList<>();
            mMethodMap.put(methodName, methods);
        }
        methods.add(method);
    }









}
