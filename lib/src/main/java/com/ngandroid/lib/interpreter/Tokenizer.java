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
        CLOSE_PARENTHESIS,
        TERNARY_QUESTION,
        TERNARY_COLON,
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
        KNOT_VALUE,
        OPERATOR,
        IN_FLOAT,
        FLOAT_END,
        DOUBLE_END,
        INTEGER_END,
        LONG_END,
        NESTED_EXPRESSION,
        IN_STRING_SLASH,
        STRING_SLASH_END,
        WHITESPACE, FLOAT_F_END
    }

    private int index, readIndex;
    private String script;
    private Queue<Token> tokens;
    private State state;

    public Tokenizer(String script) {
        this.script = script;//.replaceAll("\\s+(?=([^']*'[^']*')*[^']*$)", "");
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
                result = getNextState();
                break;
            case IN_STRING:
                if(peek() == '\\'){
                    result = State.IN_STRING_SLASH;
                }else
                    result = getNextState();
                break;
            case IN_STRING_SLASH:
                result = getNextState();
                break;
            case STRING_SLASH_END:
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
            case CLOSE_PARENTHESIS:
                emit(TokenType.CLOSE_PARENTHESIS);
                result = getNextState();
                break;
            case TERNARY_QUESTION:
                emit(TokenType.TERNARY_QUESTION_MARK);
                result = getNextState();
                break;
            case TERNARY_COLON:
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
            case IN_FLOAT: {
                if (!Character.isDigit(peek())){
                    result = State.FLOAT_END;
                }else
                    result = getNextState();
                break;
            }
            case IN_NUMBER_CONSTANT: {
                char c = peek();
                if (!Character.isDigit(c) && c != '.')
                    result = State.NUMBER_CONSTANT_END;
                else
                    result = getNextState();
                break;
            }
            case FLOAT_END: {
                char c = peek();
                if (c != 'd' && c != 'D' && c != 'f' && c != 'F')
                    emit(TokenType.FLOAT_CONSTANT);
                result = getNextState();
                break;
            }
            case NUMBER_CONSTANT_END: {
                char c = peek();
                if (c != 'l' && c != 'L' && c != 'f' && c != 'F' && c != 'd' && c != 'D')
                    emit(TokenType.INTEGER_CONSTANT);

                result = getNextState();
                break;
            }
            case FLOAT_F_END:
                emit(TokenType.FLOAT_CONSTANT);
                result = getNextState();
                break;
            case DOUBLE_END:
                emit(TokenType.DOUBLE_CONSTANT);
                result = getNextState();
                break;
            case LONG_END:
                emit(TokenType.LONG_CONSTANT);
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
            case NESTED_EXPRESSION:
                emit(TokenType.OPEN_PARENTHESIS_EXP);
                result = getNextState();
                break;
            case KNOT_EQUALS_START:
                if(peek() != '='){
                    result = State.KNOT_VALUE;
                }else{
                    result = getNextState();
                }
                break;
            case KNOT_VALUE:
                emit(TokenType.KNOT);
                result = State.IN_CHAR_SEQUENCE;
                break;
            case MODEL_FIELD_END:
                emit(TokenType.MODEL_FIELD);
                result = getNextState();
                break;
            case KNOT_EQUALS:
            case EQUALS:
            case OPERATOR:
                emit(TokenType.BINARY_OPERATOR);
                result = getNextState();
                break;
            case WHITESPACE:
                emit(TokenType.WHITESPACE);
                result = getNextState();
                break;
            default:
                throw new RuntimeException("This shouldn't happen. Please submit an issue at github.com/davityle/ngAndroid/issues");
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

            if(state == State.IN_STRING_SLASH) {
                script = script.substring(0, index) + script.substring(index + 1, script.length());
                index--;
                return State.STRING_SLASH_END;
            }
            if(state == State.STRING_SLASH_END) {
                return State.IN_STRING;
            }

            char currentCharacter = script.charAt(index);

            if(state == State.IN_STRING && currentCharacter != '\'')
                return state;

            if(state == State.IN_NUMBER_CONSTANT && currentCharacter == '.'){
                return State.IN_FLOAT;
            }

            if (Character.isDigit(currentCharacter)) {
                switch (state){
                    case IN_FLOAT:
                        return State.IN_FLOAT;
                    case IN_NUMBER_CONSTANT:
                    case FUNCTION_PARAMETER_START:
                    case OPERATOR:
                    case BEGIN:
                    case TERNARY_QUESTION:
                    case TERNARY_COLON:
                    case FUNCTION_PARAMETER_DELIMINATOR:
                    case EQUALS:
                    case KNOT_EQUALS:
                    case WHITESPACE:
                    case NESTED_EXPRESSION:
                        return State.IN_NUMBER_CONSTANT;
                    default:
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
                    case TERNARY_COLON:
                    case OPERATOR:
                    case FUNCTION_PARAMETER_START:
                    case FUNCTION_PARAMETER_DELIMINATOR:
                    case EQUALS:
                    case NESTED_EXPRESSION:
                    case KNOT_EQUALS:
                    case WHITESPACE:
                        return State.IN_CHAR_SEQUENCE;
                    case KNOT_EQUALS_START:
                        return State.KNOT_VALUE;
                    case NUMBER_CONSTANT_END:
                        if(currentCharacter == 'l' || currentCharacter == 'L')
                            return State.LONG_END;
                    case FLOAT_END:
                        if(currentCharacter == 'd' || currentCharacter == 'D')
                            return State.DOUBLE_END;
                        if(currentCharacter == 'f' || currentCharacter == 'F')
                            return State.FLOAT_F_END;
                    default:
                        throw new RuntimeException("Invalid character '" + currentCharacter + "' at state " + state.toString());
                }
            }

            switch (currentCharacter) {
                case ',':
                    return State.FUNCTION_PARAMETER_DELIMINATOR;
                case '.':
                    return state == State.IN_NUMBER_CONSTANT ? State.IN_NUMBER_CONSTANT : State.MODEL_PERIOD;
                case '(':
                    return state == State.FUNCTION_NAME_END ? State.FUNCTION_PARAMETER_START : State.NESTED_EXPRESSION;
                case ')':
                    return State.CLOSE_PARENTHESIS;
                case '?':
                    return State.TERNARY_QUESTION;
                case ':':
                    return State.TERNARY_COLON;
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

            if(Character.isWhitespace(currentCharacter)){
                return State.WHITESPACE;
            }

            throw new RuntimeException("Invalid character : " + currentCharacter + " in state " + state);
        }finally {
            advance();
        }
    }

    private char peek() {
        return index < script.length() ? script.charAt(index) : 0;
    }

    private void emit(TokenType tokenType) {
        if(tokenType != TokenType.WHITESPACE) {
            Token token = new Token(tokenType, script.substring(readIndex, index));
            tokens.add(token);
        }
        readIndex = index;
    }
}

