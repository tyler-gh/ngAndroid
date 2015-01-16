package com.ngandroid.lib.parser;

import java.util.Queue;

public class SyntaxParser {

    public interface TokenConsumer {
        public void OnValidToken(Tokenizer.Token token);
    }

    private final Queue<Tokenizer.Token> mTokens;
    private final TokenConsumer mConsumer;

    public SyntaxParser(String script, TokenConsumer consumer){
        this.mTokens = new Tokenizer(script).getTokens();
        this.mConsumer = consumer;
        System.out.println(mTokens);
    }

    public void parseScript(){
        if(!
            (
                offerPop(Tokenizer.TokenType.MODEL_NAME) ||
                offerPop(Tokenizer.TokenType.FUNCTION_NAME) ||
                offerPop(Tokenizer.TokenType.EOF)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }
    
    private boolean offerPop(Tokenizer.TokenType tokenType){
        System.out.println("offered " + tokenType);
        if(mTokens.peek().getTokenType() != tokenType){
            return false;
        }
        popToken();
        return true;
    }

    private void popToken() {
        Tokenizer.Token token = mTokens.poll();
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

    private void emit(Tokenizer.TokenType tokenType){
        if(mTokens.peek().getTokenType() != tokenType){
            throw new RuntimeException();
        }
        popToken();
    }

    private void afterModel(){
        if(!
            (
                offerPop(Tokenizer.TokenType.TERNARY_COLON) ||
                offerPop(Tokenizer.TokenType.TERNARY_QUESTION_MARK) ||
                offerPop(Tokenizer.TokenType.EOF)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void parseModel(){
        emit(Tokenizer.TokenType.PERIOD);
        emit(Tokenizer.TokenType.MODEL_FIELD);
    }

    private void parseFunction(){
        emit(Tokenizer.TokenType.OPEN_PARENTHESIS);
        continueFunction();
    }

    private void functionClose(){
        if(!
            (
                offerPop(Tokenizer.TokenType.TERNARY_COLON) ||
                offerPop(Tokenizer.TokenType.TERNARY_QUESTION_MARK) ||
                offerPop(Tokenizer.TokenType.EOF)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void continueFunction(){
        emit(Tokenizer.TokenType.FUNCTION_PARAMETER);
        if(!
            (
                offerPop(Tokenizer.TokenType.COMMA) ||
                offerPop(Tokenizer.TokenType.CLOSE_PARENTHESIS)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }

    private void parseTernary(){
        if(!
            (
                offerPop(Tokenizer.TokenType.MODEL_NAME) ||
                offerPop(Tokenizer.TokenType.FUNCTION_NAME)
            )
        ){
            // TODO error
            throw new RuntimeException();
        }
    }
}
