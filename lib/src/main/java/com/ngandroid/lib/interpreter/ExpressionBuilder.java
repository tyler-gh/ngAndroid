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

import com.ngandroid.lib.ng.getters.BinaryOperatorGetter;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.getters.KnotGetter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.ng.getters.StaticGetter;
import com.ngandroid.lib.ng.getters.TernaryGetter;
import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 2/2/15.
 */
public class ExpressionBuilder<T> {

    private final Token[] tokens;

    public ExpressionBuilder(String expression){
        this(new SyntaxParser(expression).parseScript());
    }


    public ExpressionBuilder(Token[] tokens) {
        this.tokens = tokens;
    }

    public Getter<T> build(Object scope,ModelBuilderMap builders){
        Getter[] getters = createGetters(0, tokens.length - 1, scope, tokens, builders).getFirst();
        if(getters.length != 1){
            throw new RuntimeException("Each expression can only return one value instead found "+ getters.length);
        }
        return (Getter<T>) getters[0];
    }


    private Method findMethod(String functionName, Class<?> clss, int[] types){
        Method[] methods = clss.getDeclaredMethods();
        outerLoop:
        for(Method m : methods){
            if(m.getName().toLowerCase().equals(functionName.toLowerCase())){
                Class<?>[] paramTypes = m.getParameterTypes();
                if(paramTypes.length == types.length) {
                    for (int index = 0; index < types.length; index++) {
                        if(TypeUtils.getType(paramTypes[index]) != types[index])
                            continue outerLoop;
                    }
                    return m;
                }
            }
        }
        throw new RuntimeException(new NoSuchMethodException("There is no method " + functionName + " found in " + clss.getSimpleName()) + " with " + types.length + " matching types");

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

    public Tuple<Getter[], Integer> createGetters(int startIndex, int endIndex, Object scope, Token[] tokens, ModelBuilderMap builders){
        List<Getter> getters = new ArrayList<>();
        int index = startIndex;
        while (index < endIndex){
            Token token = tokens[index];
            switch(token.getTokenType()){
                case FUNCTION_NAME:{
                    int end = findEndOfFunction(tokens, index+2);
                    Tuple<Getter[], Integer> values = createGetters(index+2, end,scope, tokens, builders);
                    Getter[] parameters = values.getFirst();
                    index = values.getSecond();
                    String functionName = token.getScript();
                    int[]paramTypes = new int[parameters.length];
                    for(int i = 0; i < paramTypes.length; i++){
                        paramTypes[i] = parameters[i].getType();
                    }
                    Method method = findMethod(functionName, scope.getClass(), paramTypes);
                    getters.add(new ClickInvoker(method, scope, parameters));
                    break;
                }
                case KNOT:{
                    Tuple<Getter[], Integer> value = createGetters(index+1, endIndex,scope, tokens, builders);
                    KnotGetter getter = new KnotGetter(value.getFirst()[0]);
                    index = value.getSecond();
                    getters.add(getter);
                    break;
                }
                case INTEGER_CONSTANT: {
                    getters.add(new StaticGetter<>(Integer.parseInt(token.getScript()), TypeUtils.INTEGER));
                    index++;
                    break;
                }
                case LONG_CONSTANT: {
                    String longStr = token.getScript();
                    getters.add(new StaticGetter<>(Long.parseLong(longStr.substring(0,longStr.length() - 1)), TypeUtils.LONG));
                    index++;
                    break;
                }
                case FLOAT_CONSTANT: {
                    getters.add(new StaticGetter<>(Float.parseFloat(token.getScript()), TypeUtils.FLOAT));
                    index++;
                    break;
                }
                case DOUBLE_CONSTANT: {
                    getters.add(new StaticGetter<>(Double.parseDouble(token.getScript()), TypeUtils.DOUBLE));
                    index++;
                    break;
                }
                case MODEL_NAME: {
                    String modelName = token.getScript();
                    String fieldName = tokens[index + 2].getScript();
                    ModelBuilder builder = builders.get(modelName);
                    getters.add(new ModelGetter(fieldName, modelName, builder.getMethodInvoker()));
                    index += 3;
                    break;
                }
                case STRING: {
                    getters.add(new StaticGetter<>(token.getScript(), TypeUtils.STRING));
                    index++;
                    break;
                }
                case CLOSE_PARENTHESIS:
                case COMMA:{
                    index++;
                    break;
                }
                case TERNARY_QUESTION_MARK: {
                    Getter<Boolean> getter =  getMostRecentGetter(getters, "Ternary Question mark cannot be the first expression.");
                    int ternaryColonIndex = findColon(tokens, index);
                    Tuple<Getter[], Integer> values = createGetters(index + 1, ternaryColonIndex, scope, tokens, builders);
                    Getter trueGetter = values.getFirst()[0];
                    values = createGetters(ternaryColonIndex + 1, endIndex, scope, tokens, builders);
                    Getter falseGetter = values.getFirst()[0];
                    index = values.getSecond();
                    getters.add(new TernaryGetter(getter, trueGetter, falseGetter));
                    break;
                }
                case BINARY_OPERATOR: {
                    Getter leftgetter =  getMostRecentGetter(getters, "Binary Operator cannot be the first expression.");
                    TokenType.BinaryOperator operator = TokenType.BinaryOperator.getOperator(token.getScript());
                    Tuple<Getter[], Integer> values = createGetters(index + 1, endIndex, scope, tokens, builders);
                    Getter rightgetter = values.getFirst()[0];
                    index = values.getSecond();
                    int lefttype = leftgetter.getType();
                    int righttype = rightgetter.getType();
                    int type;
                    if((righttype == TypeUtils.BOOLEAN || righttype == TypeUtils.OBJECT) || (lefttype == TypeUtils.BOOLEAN || lefttype == TypeUtils.OBJECT))
                        throw new RuntimeException("Types " + lefttype + " and " + righttype + " cannot be compared");

                    if(lefttype == TypeUtils.STRING || righttype == TypeUtils.STRING){
                        type = TypeUtils.STRING;
                    }else if(lefttype == TypeUtils.DOUBLE || righttype == TypeUtils.DOUBLE){
                        type = TypeUtils.DOUBLE;
                    }else if(lefttype == TypeUtils.FLOAT || righttype == TypeUtils.FLOAT){
                        type = TypeUtils.FLOAT;
                    }else if(lefttype == TypeUtils.LONG || righttype == TypeUtils.LONG){
                        type = TypeUtils.LONG;
                    }else{
                        type = TypeUtils.INTEGER;
                    }
                    getters.add(BinaryOperatorGetter.getOperator(leftgetter, rightgetter, type, operator));
                    break;
                }


                default:
                    throw new RuntimeException("Invalid token in expression: " + token.getTokenType());
            }
        }

        return Tuple.of(getters.toArray(new Getter[getters.size()]), index);
    }

    private Getter getMostRecentGetter(List<Getter> getters, String error){
        if(getters.size() == 0){
            throw new RuntimeException(error);
        }
        Getter getter =  getters.get(getters.size() - 1);
        getters.remove(getter);
        return getter;
    }

}
