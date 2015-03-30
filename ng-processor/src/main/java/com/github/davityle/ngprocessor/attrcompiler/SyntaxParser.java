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


import java.util.Queue;
import java.util.Stack;

public class SyntaxParser {

    private final Queue<Token> mTokens;
    private final Token[] mTokenArray;
    private int mTokenArrayIndex;
    private Stack<TokenType> parenthesisStack = new Stack<>();

    public SyntaxParser(String script){
        this.mTokens = new Tokenizer(script).getTokens();
        this.mTokenArray = new Token[mTokens.size()];
        this.mTokenArrayIndex = 0;
    }

    public Token[] parseScript(){
        if (!
                (
                    offerPop(TokenType.KNOT) ||
                    offerPop(TokenType.MODEL_NAME) ||
                    offerPop(TokenType.FUNCTION_NAME) ||
                    offerPop(TokenType.INTEGER_CONSTANT) ||
                    offerPop(TokenType.FLOAT_CONSTANT) ||
                    offerPop(TokenType.DOUBLE_CONSTANT) ||
                    offerPop(TokenType.LONG_CONSTANT) ||
                    offerPop(TokenType.OPEN_PARENTHESIS_EXP) ||
                    offerPop(TokenType.EOF)
                )
        ) {
            throw new RuntimeException("Invalid start of expression " + mTokens.peek());
        }
        if(mTokens.size() > 0){
            throw new RuntimeException("Dangling tokens " + mTokens.toString());
        }

        if(parenthesisStack.size() > 0){
            throw new RuntimeException("Unclosed nested expression");
        }
        return mTokenArray;
    }
    
    private boolean offerPop(TokenType tokenType){
        if(mTokens.peek().getTokenType() != tokenType){
            return false;
        }
        popToken();
        return true;
    }

    private void popToken() {
        Token token = mTokens.poll();
        mTokenArray[mTokenArrayIndex++] = token;
        switch (token.getTokenType()){
            case MODEL_NAME:
                parseModel();
                break;
            case FUNCTION_NAME:
                parseFunction();
                break;
            case COMMA:
                continueFunction();
                break;
            case TERNARY_COLON:
            case TERNARY_QUESTION_MARK:
                parseTernary();
                break;
            case STRING:
            case INTEGER_CONSTANT:
            case FLOAT_CONSTANT:
            case LONG_CONSTANT:
            case DOUBLE_CONSTANT:
            case MODEL_FIELD:
                afterModel();
                break;
            case OPEN_PARENTHESIS_EXP:
                parenthesisStack.push(TokenType.OPEN_PARENTHESIS_EXP);
                inExpression();
                break;
            case OPEN_PARENTHESIS:
                parenthesisStack.push(TokenType.OPEN_PARENTHESIS);
                break;
            case CLOSE_PARENTHESIS:
                parenthesisStack.pop();
                functionClose();
                break;
            case KNOT:
                afterKnot();
                break;
            case BINARY_OPERATOR:
                afterOperator();
                break;
        }
    }

    private void inExpression() {
        if (!
            (
                offerPop(TokenType.KNOT) ||
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.INTEGER_CONSTANT) ||
                offerPop(TokenType.FLOAT_CONSTANT) ||
                offerPop(TokenType.DOUBLE_CONSTANT) ||
                offerPop(TokenType.LONG_CONSTANT) ||
                offerPop(TokenType.OPEN_PARENTHESIS_EXP)
            )
        ){
            throw new RuntimeException("Invalid token in expression " + mTokens.peek());
        }
        emit(TokenType.CLOSE_PARENTHESIS);
    }

    private void emit(TokenType tokenType){
        if(mTokens.peek().getTokenType() != tokenType){
            Token token = mTokens.peek();
            throw new RuntimeException(token.getScript() + " is invalid. " + token.getTokenType() + " != " + tokenType);
        }
        popToken();
    }

    private void afterKnot(){
        if(!
            (
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.MODEL_NAME)
            )
        ){
            throw new RuntimeException("Parse error was expecting MODEL_NAME or FUNCTION_NAME after negation (!)");
        }
    }

    private void afterOperator(){
        if(!
            (
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.STRING) ||
                offerPop(TokenType.INTEGER_CONSTANT) ||
                offerPop(TokenType.FLOAT_CONSTANT) ||
                offerPop(TokenType.DOUBLE_CONSTANT) ||
                offerPop(TokenType.LONG_CONSTANT) ||
                offerPop(TokenType.OPEN_PARENTHESIS_EXP)
            )
        ){
            throw new RuntimeException("Invalid token after binary operator " + mTokens.peek());
        }
    }

    private void afterModel(){
        if(parenthesisStack.size() > 0 && parenthesisStack.peek() == TokenType.OPEN_PARENTHESIS){
            offerPop(TokenType.TERNARY_COLON);
            offerPop(TokenType.TERNARY_QUESTION_MARK);
            offerPop(TokenType.BINARY_OPERATOR);
        }else {
            if (!
                (
                    offerPop(TokenType.TERNARY_COLON) ||
                    offerPop(TokenType.TERNARY_QUESTION_MARK) ||
                    offerPop(TokenType.BINARY_OPERATOR) ||
                    offerPop(TokenType.EOF) ||
                    topIs(TokenType.CLOSE_PARENTHESIS)
                )
            ) {
                throw new RuntimeException("Invalid token after model "  + mTokens.peek());
            }
        }
    }

    private void parseModel(){
        emit(TokenType.PERIOD);
        emit(TokenType.MODEL_FIELD);
    }

    private void parseFunction(){
        emit(TokenType.OPEN_PARENTHESIS);
        continueFunction();
    }

    private void functionClose(){
        if(!
            (
                offerPop(TokenType.TERNARY_COLON) ||
                offerPop(TokenType.TERNARY_QUESTION_MARK) ||
                offerPop(TokenType.BINARY_OPERATOR) ||
                offerPop(TokenType.EOF) ||
                topIs(TokenType.COMMA) ||
                topIs(TokenType.CLOSE_PARENTHESIS)
            )
        ){
            throw new RuntimeException("Invalid token after function close " + mTokens.peek());
        }
    }

    private boolean topIs(TokenType tokenType) {
        return mTokens.peek().getTokenType() == tokenType;
    }

    private void continueFunction(){
        if(!
            (
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.STRING) ||
                offerPop(TokenType.INTEGER_CONSTANT) ||
                offerPop(TokenType.FLOAT_CONSTANT) ||
                offerPop(TokenType.DOUBLE_CONSTANT) ||
                offerPop(TokenType.LONG_CONSTANT) ||
                offerPop(TokenType.FUNCTION_NAME)
            )
        ){
            if(!offerPop(TokenType.CLOSE_PARENTHESIS))
                throw new RuntimeException("Invalid token in method parameter " + mTokens.peek());
            else
                return;
        }
        if(!
            (
                offerPop(TokenType.COMMA) ||
                offerPop(TokenType.CLOSE_PARENTHESIS)
            )
        ){
            throw new RuntimeException("Invalid token in method parameter " + mTokens.peek());
        }
    }

    private void parseTernary(){
        if(!
            (
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.INTEGER_CONSTANT) ||
                offerPop(TokenType.FLOAT_CONSTANT) ||
                offerPop(TokenType.DOUBLE_CONSTANT) ||
                offerPop(TokenType.LONG_CONSTANT) ||
                offerPop(TokenType.KNOT) ||
                offerPop(TokenType.STRING)
            )
        ){
            throw new RuntimeException("Invalid token in ternary operator " + mTokens.peek());
        }
    }
}
