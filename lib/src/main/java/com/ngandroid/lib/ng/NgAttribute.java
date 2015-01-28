package com.ngandroid.lib.ng;

import android.view.View;

import com.ngandroid.lib.interpreter.Token;

/**
 * Created by davityle on 1/23/15.
 */
public interface NgAttribute {
    public void typeCheck(Token[] tokens);
    public void attach(final Token[] tokens, Object mModel, ModelBuilderMap builders, View bindView) throws Exception;
}
