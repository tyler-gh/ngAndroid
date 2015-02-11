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

/**
* Created by davityle on 1/15/15.
*/
public enum TokenType {
    MODEL_NAME,
    MODEL_FIELD,
    FUNCTION_NAME,
    TERNARY_QUESTION_MARK,
    TERNARY_COLON,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    COMMA,
    PERIOD,
    BINARY_OPERATOR,
    NUMBER_CONSTANT,
    RUBBISH,
    EOF,
    STRING,
    KNOT;

    public enum BinaryOperator {
        ADDITION,
        SUBTRACTION,
        KNOT_EQUALS,
        MULTIPLICATION,
        DIVISION,
        EQUALS_EQUALS;

        public static BinaryOperator getOperator(String op){
            switch(op){
                case "+":
                    return ADDITION;
                case "-":
                    return SUBTRACTION;
                case "/":
                    return DIVISION;
                case "*":
                    return MULTIPLICATION;
                case "==":
                    return EQUALS_EQUALS;
                case "!=":
                    return KNOT_EQUALS;
                default:
                    throw new RuntimeException("Unrecognized operator");
            }
        }
    }
}


