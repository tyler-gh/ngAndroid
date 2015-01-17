package com.ngandroid.lib.parser;

import java.util.Queue;

public class SyntaxParser {

    public interface TokenConsumer {
        public void OnValidToken(Token token);
    }

    private final Queue<Token> mTokens;
    private final TokenConsumer mConsumer;

    public SyntaxParser(String script, TokenConsumer consumer){
        this.mTokens = new Tokenizer(script).getTokens();
        this.mConsumer = consumer;
        System.out.println(mTokens);
    }

    public void parseScript(){
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
    }
    
    private boolean offerPop(TokenType tokenType){
        System.out.println("offered " + tokenType);
        if(mTokens.peek().getTokenType() != tokenType){
            return false;
        }
        popToken();
        return true;
    }

    private void popToken() {
        Token token = mTokens.poll();
        mConsumer.OnValidToken(token);
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
            case MODEL_FIELD:
                afterModel();
                break;
            case CLOSE_PARENTHESIS:
                functionClose();
                break;
        }
    }

    private void emit(TokenType tokenType){
        if(mTokens.peek().getTokenType() != tokenType){
            throw new RuntimeException(mTokens.peek().getTokenType() + " != " + tokenType);
        }
        popToken();
    }

    private void afterModel(){
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
        emit(TokenType.FUNCTION_PARAMETER);
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
