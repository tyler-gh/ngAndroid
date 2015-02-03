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

package com.ngandroid.lib.interpreter;

import com.ngandroid.lib.ng.Getter;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.ModelGetter;
import com.ngandroid.lib.ng.StaticGetter;
import com.ngandroid.lib.ng.TernaryGetter;
import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by tyler on 2/2/15.
 */
public class ExpressionBuilder<T> {

    private final Token[] tokens;

    public ExpressionBuilder(Token[] tokens) {
        this.tokens = tokens;
    }

    public Expression<T> build(Object mModel,ModelBuilderMap builders){
        Getter[] getters = createGetters(0, tokens.length - 2, mModel, tokens, builders, Integer.MAX_VALUE);

        return null;
    }


    private Method findMethod(String functionName, Class<?> clss){
        Method[] methods = clss.getDeclaredMethods();
        for(Method m : methods){
            if(m.getName().toLowerCase().equals(functionName.toLowerCase())){
                return m;
            }
        }
        throw new RuntimeException(new NoSuchMethodException("There is no method " + functionName + " found in " + clss.getSimpleName()));

    }

    private int findEndOfFunction(Token[] tokens, int startIndex){
        for(int index = startIndex; index < tokens.length; index++){
            if(tokens[index].getTokenType() == TokenType.CLOSE_PARENTHESIS)
                return index;
        }
        throw new RuntimeException("Function is not closed properly");
    }

    private int findColon(Token[] tokens, int startIndex){
        for(int index = startIndex; index < tokens.length; index++){
            if(tokens[index].getTokenType() == TokenType.TERNARY_COLON)
                return index;
        }
        throw new RuntimeException("Ternary is not formed properly");
    }

    private Getter[] createGetters(int startIndex, int endPadding, Object mModel, Token[] tokens, ModelBuilderMap builders, int limit){
        ArrayList<Getter> getters = new ArrayList<>();

        int count = 0;
        int index = startIndex;
        while (index < tokens.length - endPadding && count < limit){
            Token token = tokens[index];
            count++;
            switch(token.getTokenType()){
                case FUNCTION_NAME:{
                    String functionName = token.getScript();
                    Method method = findMethod(functionName, mModel.getClass());
                    int endIndex = findEndOfFunction(tokens, index+2);
                    Getter[] parameters = createGetters(index+2, endIndex,mModel, tokens, builders, Integer.MAX_VALUE);
                    getters.add(new ClickInvoker(method, mModel, parameters));
                    break;
                }

                case NUMBER_CONSTANT: {
                    // TODO add support for floats, doubles, and longs
                    getters.add(new StaticGetter<>(Integer.parseInt(token.getScript())));
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
                    getters.add(new StaticGetter<>(token.getScript()));
                    index++;
                    break;
                }
                case COMMA:{
                    index++;
                    break;
                }
                case TERNARY_QUESTION_MARK: {
                    if(getters.size() == 0){
                        throw new RuntimeException("Ternary Question mark cannot be the first expression.");
                    }
                    Getter<Boolean> getter =  getters.get(getters.size() - 1);
                    getters.remove(getter);
                    int ternaryColonIndex = findColon(tokens, index);
                    Getter trueGetter = createGetters(index + 1, ternaryColonIndex, mModel, tokens, builders, 1)[0];
                    Getter falseGetter = createGetters(ternaryColonIndex + 1, endPadding, mModel, tokens, builders, 1)[0];
                    getters.add(new TernaryGetter(getter, trueGetter, falseGetter));
                    break;
                }
                default:
                    throw new RuntimeException("Invalid token in function parameter " + token.getTokenType());
            }
        }

        return getters.toArray(new Getter[getters.size()]);
    }

}
