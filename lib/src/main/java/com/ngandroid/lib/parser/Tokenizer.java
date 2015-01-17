package com.ngandroid.lib.parser;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by davityle on 1/13/15.
 */
public class Tokenizer {

    private enum State {
        BEGIN,
        CHAR_SEQUENCE,
        MODEL_FIELD,
        FUNCTION_PARAMETER,
        MODEL_FIELD_END,
        OPEN_PARENTHESIS,
        CLOSE_PARENTHESIS,
        END,
        COMMA,
        QUESTION_MARK,
        COLON,
        PERIOD,
        NOT_EQUALS,
        EQUALS_EQUALS,
        OPERATOR,
        DIGIT
    }

    private int index, readIndex;
    private String script;
    private Queue<Token> tokens;
    private char currentCharacter = 0;

    public Tokenizer(String script){
        this.script = script.replaceAll("\\s","");
    }

    public Queue<Token> getTokens(){
        if(tokens == null){
            generateTokens();
        }
        return tokens;
    }

    private void generateTokens() {
        tokens = new LinkedList<>();
        index = 0;
        readIndex = 0;

        State state = State.BEGIN;
        while (state != State.END) {
            state = nextState(state);
        }
        if(readIndex != index){
            emit(TokenType.RUBBISH);
        }

        tokens.add(new Token(TokenType.EOF, null));
    }

    private State nextState(State state) {
        State result;
        switch (state) {
            case BEGIN:
            case CHAR_SEQUENCE:
                result = getNextState();
                switch(peek()){
                    case '.':
                        emit(TokenType.MODEL_NAME);
                        break;
                    case '(':
                        emit(TokenType.FUNCTION_NAME);
                        break;
                }
                break;
            case QUESTION_MARK:
                //*
                emit(TokenType.TERNARY_QUESTION_MARK);
                result = getNextState();
                break;
            case COLON:
                //*
                emit(TokenType.TERNARY_COLON);
                result = getNextState();
                break;
            case PERIOD:
                result = State.MODEL_FIELD;
                emit(TokenType.PERIOD);
                break;
            case MODEL_FIELD: {
                State current = getNextState();
                if(!Character.isLetter(peek())){
                    emit(TokenType.MODEL_FIELD);
                    result = current;
                }else{
                    result = State.MODEL_FIELD;
                }
                break;
            }
            case FUNCTION_PARAMETER: {
                State current = getNextState();
                if(peek() == ')' || peek() == ','){
                    emit(TokenType.FUNCTION_PARAMETER);
                }
                if (current == State.CHAR_SEQUENCE) {
                    result = State.FUNCTION_PARAMETER;
                } else{
                    result = current;
                }
                break;
            }
            case COMMA:
                emit(TokenType.COMMA);
                result = State.FUNCTION_PARAMETER;
                break;
            case OPEN_PARENTHESIS:
                result = State.FUNCTION_PARAMETER;
                emit(TokenType.OPEN_PARENTHESIS);
                break;
            case CLOSE_PARENTHESIS:
                //*
                emit(TokenType.CLOSE_PARENTHESIS);
                result = getNextState();
                break;
            case END:
                result = State.END;
                break;
            case NOT_EQUALS:
                //*
                result = getNextState();
                emit(TokenType.KNOT_EQUALS);
                break;
            case OPERATOR:
                if(currentCharacter == '!'){
                    if(peek() == '=')
                        return State.NOT_EQUALS;
                    else {
                        emit(TokenType.KNOT);
                    }
                }else {
                    switch (currentCharacter) {
                        case '*':
                            emit(TokenType.MULTIPLICATION);
                            break;
                        case '+':
                            emit(TokenType.ADDITION);
                            break;
                        case '=':
                            if(getNextState() != State.OPERATOR || currentCharacter != '='){
                                // TODO error
                                throw new RuntimeException("There is no assignment operator in ngAndroid, use == to denote an equality check.");
                            }
                            return State.EQUALS_EQUALS;
                        case '-':
                            emit(TokenType.SUBTRACTION);
                            break;
                        case '/':
                            emit(TokenType.DIVISION);
                            break;
                    }
                }
                result = getNextState();
                break;
            case DIGIT:
                //*
                if(!Character.isDigit(peek())){
                    emit(TokenType.NUMBER_CONSTANT);
                }
                result = getNextState();
                break;
            case EQUALS_EQUALS:
                emit(TokenType.EQUALS_EQUALS);
                result = getNextState();
                break;
            default:
                // TODO error
                throw new RuntimeException();
        }
        return result;
    }

    private State getNextState() {
        if (index == script.length()) {
            return State.END;
        }

        currentCharacter = script.charAt(index++);

        if(Character.isDigit(currentCharacter)){
            System.out.println(currentCharacter + " is a digit");
            return State.DIGIT;
        }

        if (Character.isLetter(currentCharacter)) {
            return State.CHAR_SEQUENCE;
        }

        switch (currentCharacter) {
            case ',':
                return State.COMMA;
            case '.':
                return State.PERIOD;
            case '(':
                return State.OPEN_PARENTHESIS;
            case ')':
                return State.CLOSE_PARENTHESIS;
            case '?':
                return State.QUESTION_MARK;
            case ':':
                return State.COLON;
            case '!':
            case '*':
            case '+':
            case '=':
            case '-':
            case '/':
                return State.OPERATOR;
        }
        // TODO throw error
        throw new RuntimeException();
    }

    private char peek(){
       return index < script.length() ? script.charAt(index) : 0;
    }

    private void emit(TokenType tokenType) {
        Token token = new Token(tokenType, script.substring(readIndex, index));
        readIndex = index;
        tokens.add(token);
    }


    // * order is important

}
