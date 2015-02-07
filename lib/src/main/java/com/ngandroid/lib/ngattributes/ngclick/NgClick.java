/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ngandroid.lib.ngattributes.ngclick;

import android.view.View;

import com.ngandroid.lib.interpreter.ExpressionBuilder;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.ng.Getter;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.ModelGetter;
import com.ngandroid.lib.ng.NgAttribute;
import com.ngandroid.lib.ng.StaticGetter;
import com.ngandroid.lib.ngattributes.nglongclick.LongClickInvoker;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

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

    @Override
    public void typeCheck(Token[] tokens) {
        TypeUtils.startsWith(tokens, TokenType.FUNCTION_NAME);
        TypeUtils.endsWith(tokens, TokenType.CLOSE_PARENTHESIS);
    }

    @Override
    public void attach(Token[] tokens, final Object mModel, ModelBuilderMap builders, View bindView) {
        attach(tokens, mModel, builders, bindView, false);
    }

    public ClickInvoker getInvoker(Token[] tokens, final Object mModel, ModelBuilderMap builders, int functionNameIndex, int parametersEndPadding){
        String functionName = tokens[functionNameIndex].getScript();
        Method method = findMethod(functionName, mModel.getClass());
        if(method == null){
            // TODO error
            throw new RuntimeException(new NoSuchMethodException("There is no method " + functionName + " found in " + mModel.getClass().getSimpleName()));
        }
        Getter[] parameters =  createParameters(functionNameIndex+2, parametersEndPadding, tokens, builders);
        return new ClickInvoker(method, mModel, parameters);
    }

    public void attach(Token[] tokens, final Object mModel, ModelBuilderMap builders, View bindView, boolean isLongClick){
        typeCheck(tokens);
        ClickInvoker invoker = getInvoker(tokens, mModel, builders, 0, 2);
        if(!isLongClick)
            bindView.setOnClickListener(invoker);
        else
            bindView.setOnLongClickListener(new LongClickInvoker(invoker));
    }

    private Method findMethod(String functionName, Class<?> clss){
        Method[] methods = clss.getDeclaredMethods();
        for(Method m : methods){
            if(m.getName().toLowerCase().equals(functionName.toLowerCase())){
                return m;
            }
        }
        return null;
    }

    // TODO add support for ternary operators and functions
    private Getter[] createParameters(int startIndex, int endPadding, Token[] tokens, ModelBuilderMap builders){
        ArrayList<Getter> getters = new ArrayList<>();

        int index = startIndex;
        while (index < tokens.length - endPadding){
            Token token = tokens[index];
            switch(token.getTokenType()){
                case NUMBER_CONSTANT: {
                    // TODO add support for floats, doubles, and longs
                    getters.add(new StaticGetter(Integer.parseInt(token.getScript()), TypeUtils.INTEGER));
                    index++;
                    break;
                }
                case MODEL_NAME: {
                    String modelName = token.getScript();
                    String fieldName = tokens[index + 2].getScript();
                    ModelBuilder builder = builders.get(modelName);
                    getters.add(new ModelGetter(fieldName, builder.getMethodInvoker()));
                    index += 3;
                    break;
                }
                case STRING: {
                    getters.add(new StaticGetter(token.getScript(), TypeUtils.STRING));
                    index++;
                    break;
                }
                case COMMA:{
                    index++;
                    break;
                }
                default:
                    throw new RuntimeException("Invalid token in function parameter " + token.getTokenType());
            }
        }

        return getters.toArray(new Getter[getters.size()]);
    }

}
