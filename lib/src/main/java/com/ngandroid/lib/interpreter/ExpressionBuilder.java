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

import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.getters.BinaryOperatorGetter;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.KnotGetter;
import com.ngandroid.lib.ng.getters.ModelGetter;
import com.ngandroid.lib.ng.getters.StaticGetter;
import com.ngandroid.lib.ng.getters.TernaryGetter;
import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;
import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
        Getter getter = createGetter(0, tokens.length - 1, scope, tokens, builders).getFirst();
        return (Getter<T>) getter;
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

    private int findCloseParenthesis(int startIndex) {
        int openCount = 1;
        for(int index = startIndex; index < tokens.length; index++){
            Token token = tokens[index];
            if(token.getTokenType() == TokenType.OPEN_PARENTHESIS_EXP || token.getTokenType() == TokenType.OPEN_PARENTHESIS){
                openCount++;
            }else if(token.getTokenType() == TokenType.CLOSE_PARENTHESIS) {
                if(--openCount == 0)
                    return index;
            }
        }
        throw new RuntimeException("Nested expression is not closed properly " + openCount);
    }

    private int findEndOfParameter(Token[] tokens, int startIndex){
        for(int index = startIndex; index < tokens.length; index++){
            Token token = tokens[index];
            if(token.getTokenType() == TokenType.CLOSE_PARENTHESIS || token.getTokenType() == TokenType.COMMA)
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

    public Tuple<Getter, Integer> createGetter(int startIndex, int endIndex, Object scope, Token[] tokens, ModelBuilderMap builders){
        List<Getter> getterList = new ArrayList<>();
        List<TokenType.BinaryOperator> operatorList = new ArrayList<>();
        int index = startIndex;
        while (index < endIndex){
            Token token = tokens[index];
            switch(token.getTokenType()){
                case FUNCTION_NAME:{
                    index += 2;
                    int end = findEndOfFunction(tokens, index);
                    List<Getter> parameters = new ArrayList<>();
                    while(index < end) {
                        int nextIndex = findEndOfParameter(tokens, index);
                        Tuple<Getter, Integer> values = createGetter(index, nextIndex, scope, tokens, builders);
                        parameters.add(values.getFirst());
                        index = values.getSecond();
                    }
                    String functionName = token.getScript();
                    int[] paramTypes = new int[parameters.size()];
                    for (int i = 0; i < paramTypes.length; i++) {
                        paramTypes[i] = parameters.get(i).getType();
                    }
                    Method method = findMethod(functionName, scope.getClass(), paramTypes);
                    getterList.add(new ClickInvoker(method, scope, parameters.toArray(new Getter[parameters.size()])));
                    break;
                }
                case KNOT:{
                    Tuple<Getter, Integer> value = createGetter(index + 1, endIndex, scope, tokens, builders);
                    KnotGetter getter = new KnotGetter(value.getFirst());
                    index = value.getSecond();
                    getterList.add(getter);
                    break;
                }
                case INTEGER_CONSTANT: {
                    getterList.add(new StaticGetter<>(Integer.parseInt(token.getScript()), TypeUtils.INTEGER));
                    index++;
                    break;
                }
                case LONG_CONSTANT: {
                    String longStr = token.getScript();
                    getterList.add(new StaticGetter<>(Long.parseLong(longStr.substring(0, longStr.length() - 1)), TypeUtils.LONG));
                    index++;
                    break;
                }
                case FLOAT_CONSTANT: {
                    getterList.add(new StaticGetter<>(Float.parseFloat(token.getScript()), TypeUtils.FLOAT));
                    index++;
                    break;
                }
                case DOUBLE_CONSTANT: {
                    getterList.add(new StaticGetter<>(Double.parseDouble(token.getScript()), TypeUtils.DOUBLE));
                    index++;
                    break;
                }
                case MODEL_NAME: {
                    String modelName = token.getScript();
                    String fieldName = tokens[index + 2].getScript();
                    ModelBuilder builder = builders.get(modelName);
                    getterList.add(new ModelGetter(fieldName, modelName, builder.getMethodInvoker()));
                    index += 3;
                    break;
                }
                case STRING: {
                    getterList.add(new StaticGetter<>(token.getScript(), TypeUtils.STRING));
                    index++;
                    break;
                }
                case OPEN_PARENTHESIS_EXP:{
                    index++;
                    int end = findCloseParenthesis(index);
                    Tuple<Getter, Integer> values = createGetter(index, end, scope, tokens, builders);
                    getterList.add(values.getFirst());
                    index = values.getSecond();
                    break;
                }
                case CLOSE_PARENTHESIS:
                case COMMA:{
                    index++;
                    break;
                }
                case TERNARY_QUESTION_MARK: {
                    Getter<Boolean> getter =  getMostRecentGetter(getterList, "Ternary Question mark cannot be the first expression.");
                    int ternaryColonIndex = findColon(tokens, index);
                    Tuple<Getter, Integer> values = createGetter(index + 1, ternaryColonIndex, scope, tokens, builders);
                    Getter trueGetter = values.getFirst();
                    values = createGetter(ternaryColonIndex + 1, endIndex, scope, tokens, builders);
                    Getter falseGetter = values.getFirst();
                    index = values.getSecond();
                    getterList.add(new TernaryGetter(getter, trueGetter, falseGetter));
                    break;
                }
                case BINARY_OPERATOR: {

                    TokenType.BinaryOperator operator = TokenType.BinaryOperator.getOperator(token.getScript());

                    if(operator.equals(TokenType.BinaryOperator.EQUALS_EQUALS) || operator.equals(TokenType.BinaryOperator.KNOT_EQUALS)){
                        Getter leftgetter = getMostRecentGetter(getterList, "Binary Operator cannot be the first expression.");
                        Tuple<Getter, Integer> values = createGetter(index + 1, endIndex, scope, tokens, builders);
                        Getter rightgetter = values.getFirst();
                        index = values.getSecond();
                        getterList.add(compareGetters(leftgetter, rightgetter, operator));
                    }else{
                        operatorList.add(operator);
                        index ++;
                    }
                    break;
                }
                default:
                    throw new RuntimeException("Invalid token in expression: " + token.getTokenType());
            }
        }

        if(operatorList.size() > 0){
            return Tuple.of(evaluatePostFixExpression(PostfixConverter.convertToPostfix(convertToInfix(getterList, operatorList))), index);
        }

        if(getterList.size() != 1){
            throw new RuntimeException("Each expression can only return one value instead found "+ getterList.size());
        }

        return Tuple.of(getterList.get(0), index);
    }

    private Getter compareGetters(Getter leftgetter, Getter rightgetter, TokenType.BinaryOperator operator){
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
        return BinaryOperatorGetter.getOperator(leftgetter, rightgetter, type, operator);
    }

    private Getter getMostRecentGetter(List<Getter> getters, String error){
        if(getters.size() == 0){
            throw new RuntimeException(error);
        }
        Getter getter =  getters.get(getters.size() - 1);
        getters.remove(getter);
        return getter;
    }

    public Getter evaluatePostFixExpression(Object[] postfixExpression){
        Stack<Getter> stack = new Stack<>();
        for (Object obj : postfixExpression){
            if (obj instanceof Getter){
                stack.push((Getter) obj);
            }
            else{
                Getter rightGetter = stack.pop();
                Getter leftGetter = stack.pop();
                stack.push(compareGetters(leftGetter, rightGetter, (TokenType.BinaryOperator) obj));
            }
        }
        if(stack.size() != 1){
            // TODO error
            throw new RuntimeException("Unequal distribution of binary operators");
        }

        return stack.pop();
    }

    private List<Object> convertToInfix(List<Getter> getters, List<TokenType.BinaryOperator> operators){
        List<Object> infixExpression = new ArrayList<>();
        Iterator<Getter> getterListIterator = getters.listIterator();
        Iterator<TokenType.BinaryOperator> operatorIterator = operators.listIterator();
        infixExpression.add(getterListIterator.next());
        while(getterListIterator.hasNext()){
            infixExpression.add(operatorIterator.next());
            infixExpression.add(getterListIterator.next());
        }
        return infixExpression;
    }

}
