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

import java.util.Queue;

public class SyntaxParser {

    private final Queue<Token> mTokens;
    private final Token[] mTokenArray;
    private int mTokenArrayIndex;
    private boolean inFunction;

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
                    offerPop(TokenType.EOF)
                )
        ) {
            // TODO error
            throw new RuntimeException();
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
            case NUMBER_CONSTANT:
            case MODEL_FIELD:
                afterModel();
                break;
            case OPEN_PARENTHESIS:
                inFunction = true;
                break;
            case CLOSE_PARENTHESIS:
                inFunction = false;
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
            // TODO error
            throw new RuntimeException();
        }
    }

    private void afterOperator(){
        if(!
            (
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.STRING) ||
                offerPop(TokenType.NUMBER_CONSTANT)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void afterModel(){
        if(inFunction){
            offerPop(TokenType.TERNARY_COLON);
            offerPop(TokenType.TERNARY_QUESTION_MARK);
            offerPop(TokenType.BINARY_OPERATOR);
        }else {
            if (!
                (
                    offerPop(TokenType.TERNARY_COLON) ||
                    offerPop(TokenType.TERNARY_QUESTION_MARK) ||
                    offerPop(TokenType.BINARY_OPERATOR) ||
                    offerPop(TokenType.EOF)
                )
            ) {
                // TODO error
                throw new RuntimeException();
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
            // TODO error
            throw new RuntimeException();
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
                offerPop(TokenType.NUMBER_CONSTANT) ||
                offerPop(TokenType.FUNCTION_NAME)
            )
        ){
            // TODO error
            if(!offerPop(TokenType.CLOSE_PARENTHESIS))
                throw new RuntimeException();
            else
                return;
        }
        if(!
            (
                offerPop(TokenType.COMMA) ||
                offerPop(TokenType.CLOSE_PARENTHESIS)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void parseTernary(){
        if(!
            (
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.NUMBER_CONSTANT) ||
                offerPop(TokenType.STRING)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }
}
