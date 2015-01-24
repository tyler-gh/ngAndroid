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
        if(!
            (
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.FUNCTION_NAME) ||
                offerPop(TokenType.EOF)
            )
        ){
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
            case OPERATOR:
                afterOperator();
                break;
        }
    }

    private void emit(TokenType tokenType){
        if(mTokens.peek().getTokenType() != tokenType){
            throw new RuntimeException(mTokens.peek().getTokenType() + " != " + tokenType);
        }
        popToken();
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
        if(inFunction)
            return;
        if(!
            (
                offerPop(TokenType.TERNARY_COLON) ||
                offerPop(TokenType.TERNARY_QUESTION_MARK) ||
                offerPop(TokenType.OPERATOR) ||
                offerPop(TokenType.EOF)
            )
        ){
            // TODO error
            throw new RuntimeException();
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
                offerPop(TokenType.EOF)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void continueFunction(){
        if(!
            (
                offerPop(TokenType.MODEL_NAME) ||
                offerPop(TokenType.STRING) ||
                offerPop(TokenType.NUMBER_CONSTANT)
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
                offerPop(TokenType.FUNCTION_NAME)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }
}
