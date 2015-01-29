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

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by davityle on 1/13/15.
 *
 */

public class Tokenizer {

    private enum State{
        BEGIN,
        END,
        MODEL_PERIOD,
        FUNCTION_PARAMETER_START,
        FUNCTION_PARAMETER_END,
        TERNARY_QUESTION,
        TERNAY_COLON,
        STRING_START,
        STRING_END,
        IN_STRING,
        IN_NUMBER_CONSTANT,
        KNOT_EQUALS,
        EQUALS_START,
        FUNCTION_PARAMETER_DELIMINATOR,
        EQUALS,
        KNOT_EQUALS_START,
        MODEL_NAME_END,
        MODEL_FIELD_END,
        FUNCTION_NAME_END,
        NUMBER_CONSTANT_END,
        IN_CHAR_SEQUENCE,
        IN_MODEL_FIELD,
        OPERATOR
    }

    private int index, readIndex;
    private String script;
    private Queue<Token> tokens;
    private State state;

    public Tokenizer(String script) {
        this.script = script.replaceAll("\\s+(?=([^']*'[^']*')*[^']*$)", "");
    }

    public Queue<Token> getTokens() {
        if (tokens == null) {
            generateTokens();
        }
        return tokens;
    }

    private void generateTokens() {
        tokens = new LinkedList<>();
        index = 0;
        readIndex = 0;

        state = State.BEGIN;
        while (state != State.END) {
            state = nextState();
        }
        if (readIndex != script.length()) {
            emit(TokenType.RUBBISH);
        }

        tokens.add(new Token(TokenType.EOF, null));
    }

    private State nextState() {
        State result;
        switch (state){
            case BEGIN:
            case EQUALS_START:
            case KNOT_EQUALS_START:
            case IN_STRING:
                result = getNextState();
                break;
            case IN_CHAR_SEQUENCE: {
                char c = peek();
                switch (c) {
                    case '.':
                        result = State.MODEL_NAME_END;
                        break;
                    case '(':
                        result = State.FUNCTION_NAME_END;
                        break;
                    default:
                        result = getNextState();
                        break;
                }
                break;
            }
            case END:
                result = State.END;
                break;
            case MODEL_NAME_END:
                emit(TokenType.MODEL_NAME);
                result = getNextState();
                break;
            case FUNCTION_NAME_END:
                emit(TokenType.FUNCTION_NAME);
                result = getNextState();
                break;
            case MODEL_PERIOD:
                emit(TokenType.PERIOD);
                result = State.IN_MODEL_FIELD;
                break;
            case FUNCTION_PARAMETER_START:
                emit(TokenType.OPEN_PARENTHESIS);
                result = getNextState();
                break;
            case FUNCTION_PARAMETER_END:
                emit(TokenType.CLOSE_PARENTHESIS);
                result = getNextState();
                break;
            case TERNARY_QUESTION:
                emit(TokenType.TERNARY_QUESTION_MARK);
                result = getNextState();
                break;
            case TERNAY_COLON:
                emit(TokenType.TERNARY_COLON);
                result = getNextState();
                break;
            case STRING_START:
                result = State.IN_STRING;
                break;
            case STRING_END:
                emit(TokenType.STRING);
                result = getNextState();
                break;
            case IN_NUMBER_CONSTANT: {
                char c = peek();
                if (!Character.isDigit(c) && c != '.')
                    result = State.NUMBER_CONSTANT_END;
                else
                    result = getNextState();
                break;
            }
            case NUMBER_CONSTANT_END:
                emit(TokenType.NUMBER_CONSTANT);
                result = getNextState();
                break;
            case FUNCTION_PARAMETER_DELIMINATOR:
                emit(TokenType.COMMA);
                result = getNextState();
                break;
            case IN_MODEL_FIELD:
                if(!Character.isLetter(peek())){
                    result = State.MODEL_FIELD_END;
                }else{
                    result = getNextState();
                }
                break;
            case MODEL_FIELD_END:
                emit(TokenType.MODEL_FIELD);
                result = getNextState();
                break;
            case KNOT_EQUALS:
            case EQUALS:
            case OPERATOR:
                emit(TokenType.OPERATOR);
                result = getNextState();
                break;
            default:
                throw new RuntimeException("This shouldn't happen");
        }
        return result;
    }

    private void advance() {
        index++;
    }

    private State getNextState() {
        try {
            if (index >= script.length()) {
                return State.END;
            }

            char currentCharacter = script.charAt(index);

            if (Character.isDigit(currentCharacter)) {
                switch (state){
                    case IN_NUMBER_CONSTANT:
                    case FUNCTION_PARAMETER_START:
                    case OPERATOR:
                    case BEGIN:
                    case TERNARY_QUESTION:
                    case TERNAY_COLON:
                    case FUNCTION_PARAMETER_DELIMINATOR:
                    case EQUALS:
                    case KNOT_EQUALS:
                        return State.IN_NUMBER_CONSTANT;
                    default:
                        // TODO error
                        throw new RuntimeException("Invalid character '" + currentCharacter + "' at state " + state.toString());
                }
            }

            if (Character.isLetter(currentCharacter)) {
                switch (state) {
                    case IN_MODEL_FIELD:
                    case IN_STRING:
                    case MODEL_NAME_END:
                    case FUNCTION_NAME_END:
                    case MODEL_FIELD_END:
                        return state;
                    case BEGIN:
                    case IN_CHAR_SEQUENCE:
                    case TERNARY_QUESTION:
                    case TERNAY_COLON:
                    case OPERATOR:
                    case FUNCTION_PARAMETER_START:
                    case FUNCTION_PARAMETER_DELIMINATOR:
                    case EQUALS:
                    case KNOT_EQUALS:
                        return State.IN_CHAR_SEQUENCE;
                    default:
                        // TODO error
                        throw new RuntimeException("Invalid character '" + currentCharacter + "' at state " + state.toString());
                }
            }

            switch (currentCharacter) {
                case ',':
                    return State.FUNCTION_PARAMETER_DELIMINATOR;
                case '.':
                    return state == State.IN_NUMBER_CONSTANT ? State.IN_NUMBER_CONSTANT : State.MODEL_PERIOD;
                case '(':
                    return State.FUNCTION_PARAMETER_START;
                case ')':
                    return State.FUNCTION_PARAMETER_END;
                case '?':
                    return State.TERNARY_QUESTION;
                case ':':
                    return State.TERNAY_COLON;
                case '\'':
                    return state == State.IN_STRING ? State.STRING_END : State.STRING_START;
                case '!':
                    return State.KNOT_EQUALS_START;
                case '=':
                    return state == State.KNOT_EQUALS_START ? State.KNOT_EQUALS : state == State.EQUALS_START ? State.EQUALS : State.EQUALS_START;
                case '*':
                case '+':
                case '-':
                case '/':
                    return State.OPERATOR;
            }
            if(state == State.IN_STRING)
                return state;
            //        TODO throw error
            throw new RuntimeException("Invalid character : " + currentCharacter);
        }finally {
            advance();
        }
    }

    private char peek() {
        return index < script.length() ? script.charAt(index) : 0;
    }

    private void emit(TokenType tokenType) {
        Token token = new Token(tokenType, script.substring(readIndex, index));
        readIndex = index;
        tokens.add(token);
    }
}
