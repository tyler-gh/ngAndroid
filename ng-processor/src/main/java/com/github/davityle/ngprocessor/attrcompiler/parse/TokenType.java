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
    INTEGER_CONSTANT,
    RUBBISH,
    EOF,
    STRING,
    KNOT,
    LONG_CONSTANT,
    FLOAT_CONSTANT,
    DOUBLE_CONSTANT,
    OPEN_PARENTHESIS_EXP, WHITESPACE;

    public enum BinaryOperator {
        ADDITION("+"),
        SUBTRACTION("-"),
        KNOT_EQUALS("!="),
        MULTIPLICATION("*"),
        DIVISION("/"),
        EQUALS_EQUALS("==");

        final String source;

        BinaryOperator(String source){
            this.source = source;
        }

        @Override
        public String toString() {
            return source;
        }

        public static BinaryOperator getOperator(String op){
            for(BinaryOperator biOp : values()){
                if(biOp.source.equals(op))
                    return biOp;
            }
            throw new RuntimeException("Unrecognized operator '" + op + "'");
        }
    }
}


