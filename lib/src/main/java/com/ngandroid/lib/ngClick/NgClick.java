package com.ngandroid.lib.ngClick;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by davityle on 1/23/15.
 */
public class NgClick implements NgAttribute {
    private static NgClick ourInstance = new NgClick();

    public static NgClick getInstance() {
        return ourInstance;
    }

    private NgClick() {
    }

    private static class ClickInvoker implements View.OnClickListener {

        private final Method mMethod;
        private final Object mModel;

        private ClickInvoker(Method method, Object model) {
            this.mMethod = method;
            this.mModel = model;
            method.setAccessible(true);
        }

        @Override
        public void onClick(View view) {
            try {
                mMethod.invoke(mModel);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // TODO error
                e.printStackTrace();
            }
        }
    }

    @Override
    public void typeCheck(Token[] tokens) {
        TypeUtils.startsWith(tokens, TokenType.FUNCTION_NAME);
        TypeUtils.endsWith(tokens, TokenType.CLOSE_PARENTHESIS);
        TypeUtils.looseTypeCheck(tokens, TokenType.FUNCTION_NAME, TokenType.OPEN_PARENTHESIS, TokenType.CLOSE_PARENTHESIS);
    }

    @Override
    public void attach(Token[] tokens, final Object mModel, ModelBuilderMap builders, View bindView) {
        typeCheck(tokens);

        String functionName = tokens[0].getScript();
        final Method method;
        try {

            method = mModel.getClass().getDeclaredMethod(functionName);
            method.setAccessible(true);

        } catch (NoSuchMethodException e) {
            // TODO error
            throw new RuntimeException(e);
        }
        // TODO cleanup
        bindView.setOnClickListener(new ClickInvoker(method, mModel));
    }
}
