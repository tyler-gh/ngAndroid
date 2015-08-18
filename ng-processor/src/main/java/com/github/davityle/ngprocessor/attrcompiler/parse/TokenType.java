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

package com.github.davityle.ngprocessor.attrcompiler.parse;


/**
* Created by davityle on 1/15/15.
*/
public enum TokenType {
    NONE,
    IDENTIFIER,
    TERNARY_QUESTION_MARK,
    TERNARY_COLON,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    COMMA,
    PERIOD,
    BINARY_OPERATOR,
    UNARY_OPERATOR,
    RUBBISH,
    EOF,
    STRING,
    INT_CONSTANT,
    LONG_CONSTANT,
    FLOAT_CONSTANT,
    DOUBLE_CONSTANT,
    WHITESPACE;

    public static final int TERNARY_PRECEDENCE = 1;

    public  static boolean isNumberConstant(TokenType type) {
        return type == INT_CONSTANT || type == LONG_CONSTANT || type == FLOAT_CONSTANT || type == DOUBLE_CONSTANT;
    }

    public  static boolean isPostfixOperator(TokenType type) {
        return type == OPEN_PARENTHESIS || type == PERIOD;
    }

    public enum BinaryOperator {
        MULTIPLICATION("*", 5),
        DIVISION("/", 5),
        ADDITION("+", 4),
        SUBTRACTION("-", 4),
        KNOT_EQUALS("!=", 3),
        EQUALS_EQUALS("==", 3),
        LESS_THAN("<", 2),
        GREATER_THAN(">", 2),
        LESS_EQUAL_THAN("<=", 2),
        GREATER_EQUAL_THAN(">=", 2),
        AND("&&", 1),
        OR("||", 1);

        final String source;
        final int precedence;

        BinaryOperator(String source, int precedence){
            this.source = source;
            this.precedence = precedence;
        }

        public int getPrecedence() {
            return precedence;
        }

        @Override
        public String toString() {
            return source;
        }

        public static boolean isBinaryOperator(String source) {
            for (BinaryOperator op : BinaryOperator.values()) {
                if (op.source.equals(source)) {
                    return true;
                }
            }

            return false;
        }

        public static BinaryOperator getOperator(String op){
            for(BinaryOperator biOp : values()){
                if(biOp.source.equals(op))
                    return biOp;
            }
            throw new RuntimeException("Unrecognized operator '" + op + "'");
        }
    }

    public enum UnaryOperator {
        NEGATE("-"),
        NOT("!");

        final String source;

        UnaryOperator(String source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return source;
        }

        public static boolean isUnaryOperator(String source) {
            for (UnaryOperator op : UnaryOperator.values()) {
                if (op.source.equals(source)) {
                    return true;
                }
            }

            return false;
        }

        public static UnaryOperator getOperator(String source) {
            for (UnaryOperator op : UnaryOperator.values()) {
                if (op.source.equals(source)) {
                    return op;
                }
            }

            throw new RuntimeException("Unrecognized operator '" + source + "'");
        }
    }
}


