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
        System.out.println(token);
        mConsumer.OnValidToken(token);
        switch (token.getTokenType()){
            case MODEL_NAME:
                parseModel();
                break;
            case FUNCTION_NAME:
                parseFunction();
                break;
            case FUNCTION_PARAMETER:
                continueFunction();
                break;
            case TERNARY_COLON:
            case TERNARY_QUESTION_MARK:
                parseTernary();
                break;
            case MODEL_FIELD:
                afterModel();
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
        emit(Tokenizer.TokenType.MODEL_FIELD);
    }

    private void parseFunction(){
        emit(Tokenizer.TokenType.FUNCTION_PARAMETER);
    }

    private void continueFunction(){
        if(!
            (
                offerPop(Tokenizer.TokenType.FUNCTION_PARAMETER) ||
                offerPop(Tokenizer.TokenType.TERNARY_COLON) ||
                offerPop(Tokenizer.TokenType.TERNARY_QUESTION_MARK) ||
                offerPop(Tokenizer.TokenType.EOF)
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
