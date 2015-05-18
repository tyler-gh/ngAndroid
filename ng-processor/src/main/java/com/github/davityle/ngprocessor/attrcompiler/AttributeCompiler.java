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

package com.github.davityle.ngprocessor.attrcompiler;


import com.github.davityle.ngprocessor.attrcompiler.parse.SyntaxParser;
import com.github.davityle.ngprocessor.attrcompiler.parse.Token;
import com.github.davityle.ngprocessor.attrcompiler.parse.TokenType;
import com.github.davityle.ngprocessor.attrcompiler.sources.BinaryOperatorSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.KnotSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.StaticSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.TernarySource;
import com.github.davityle.ngprocessor.util.Tuple;
import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by tyler on 2/2/15.
 */
public class AttributeCompiler {

    private final Token[] tokens;

    public AttributeCompiler(String expression){
        this(new SyntaxParser(expression).parseScript());
    }

    public AttributeCompiler(Token[] tokens) {
        this.tokens = tokens;
    }

    public Source compile(){
        return createSource(0, tokens.length - 1, tokens).getFirst();
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

    public Tuple<Source, Integer> createSource(int startIndex, int endIndex, Token[] tokens){
        List<Source> sourceList = new ArrayList<Source>();
        List<TokenType.BinaryOperator> operatorList = new ArrayList<TokenType.BinaryOperator>();
        int index = startIndex;
        while (index < endIndex){
            Token token = tokens[index];
            switch(token.getTokenType()){
                case FUNCTION_NAME:{
                    index += 2;
                    int end = findEndOfFunction(tokens, index);
                    List<Source> parameters = new ArrayList<Source>();
                    while(index < end) {
                        int nextIndex = findEndOfParameter(tokens, index);
                        Tuple<Source, Integer> values = createSource(index, nextIndex, tokens);
                        parameters.add(values.getFirst());
                        index = values.getSecond() + 1;
                    }
                    String methodName = token.getScript();
                    sourceList.add(new MethodSource(methodName, parameters));
                    break;
                }
                case KNOT:{
                    // TODO take a closer look at whether or not this can trail off and grab multiple getters
                    Tuple<Source, Integer> value = createSource(index + 1, endIndex, tokens);
                    KnotSource getter = new KnotSource(value.getFirst());
                    index = value.getSecond();
                    sourceList.add(getter);
                    break;
                }
                case INTEGER_CONSTANT: {
                    sourceList.add(new StaticSource(token.getScript(), TypeUtils.getIntegerType()));
                    index++;
                    break;
                }
                case LONG_CONSTANT: {
                    sourceList.add(new StaticSource(token.getScript(), TypeUtils.getLongType()));
                    index++;
                    break;
                }
                case FLOAT_CONSTANT: {
                    // TODO test all the number constants
                    String script = token.getScript();
                    if(!script.endsWith("f") && !script.endsWith("F"))
                        script += "f";
                    sourceList.add(new StaticSource(script, TypeUtils.getFloatType()));
                    index++;
                    break;
                }
                case DOUBLE_CONSTANT: {
                    sourceList.add(new StaticSource(token.getScript(), TypeUtils.getDoubleType()));
                    index++;
                    break;
                }
                case MODEL_NAME: {
                    String modelName = token.getScript();
                    String fieldName = tokens[index + 2].getScript();
                    sourceList.add(new ModelSource(modelName, fieldName));
                    index += 3;
                    break;
                }
                case STRING: {
                    String script = token.getScript().replace("\"", "\\\"").replace("\\'","'");
                    sourceList.add(new StaticSource('"' + script.substring(1, script.length() - 1) + '"', TypeUtils.getStringType()));
                    index++;
                    break;
                }
                case OPEN_PARENTHESIS_EXP:{
                    index++;
                    int end = findCloseParenthesis(index);
                    Tuple<Source, Integer> values = createSource(index, end, tokens);
                    sourceList.add(values.getFirst());
                    index = values.getSecond();
                    break;
                }
                case CLOSE_PARENTHESIS:
                case COMMA:{
                    index++;
                    break;
                }
                case TERNARY_QUESTION_MARK: {
                    Source source =  getMostRecentGetter(sourceList, "Ternary Question mark cannot be the first expression.");
                    int ternaryColonIndex = findColon(tokens, index);
                    Tuple<Source, Integer> values = createSource(index + 1, ternaryColonIndex, tokens);
                    Source trueSource = values.getFirst();
                    values = createSource(ternaryColonIndex + 1, endIndex, tokens);
                    Source falseSource = values.getFirst();
                    index = values.getSecond();
                    sourceList.add(new TernarySource(source, trueSource, falseSource));
                    break;
                }
                case BINARY_OPERATOR: {

                    TokenType.BinaryOperator operator = TokenType.BinaryOperator.getOperator(token.getScript());

                    if(operator.equals(TokenType.BinaryOperator.EQUALS_EQUALS) || operator.equals(TokenType.BinaryOperator.KNOT_EQUALS)){
                        Source leftgetter = getMostRecentGetter(sourceList, "Binary Operator cannot be the first expression.");
                        Tuple<Source, Integer> values = createSource(index + 1, endIndex, tokens);
                        Source rightgetter = values.getFirst();
                        index = values.getSecond();
                        sourceList.add(compareGetters(leftgetter, rightgetter, operator));
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
            return Tuple.of(evaluatePostFixExpression(PostfixConverter.convertToPostfix(convertToInfix(sourceList, operatorList))), index);
        }

        if(sourceList.size() != 1){
            throw new RuntimeException("Each expression can only return one value instead found "+ sourceList.size());
        }

        return Tuple.of(sourceList.get(0), index);
    }

    private Source compareGetters(Source leftgetter, Source rightgetter, TokenType.BinaryOperator operator){
        return BinaryOperatorSource.getOperator(leftgetter, rightgetter, operator);
    }

    private Source getMostRecentGetter(List<Source> sources, String error){
        if(sources.size() == 0){
            throw new RuntimeException(error);
        }
        Source source =  sources.get(sources.size() - 1);
        sources.remove(source);
        return source;
    }

    public Source evaluatePostFixExpression(Object[] postfixExpression){
        Stack<Source> stack = new Stack<Source>();
        for (Object obj : postfixExpression){
            if (obj instanceof Source){
                stack.push((Source) obj);
            }else{
                Source rightSource = stack.pop();
                Source leftSource = stack.pop();
                stack.push(compareGetters(leftSource, rightSource, (TokenType.BinaryOperator) obj));
            }
        }
        if(stack.size() != 1){
            throw new RuntimeException("Unequal distribution of binary operators");
        }

        return stack.pop();
    }

    private List<Object> convertToInfix(List<Source> sources, List<TokenType.BinaryOperator> operators){
        List<Object> infixExpression = new ArrayList<Object>();
        Iterator<Source> getterListIterator = sources.listIterator();
        Iterator<TokenType.BinaryOperator> operatorIterator = operators.listIterator();
        infixExpression.add(getterListIterator.next());
        while(getterListIterator.hasNext()){
            infixExpression.add(operatorIterator.next());
            infixExpression.add(getterListIterator.next());
        }
        return infixExpression;
    }

}
