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
    OPERATOR,
    NUMBER_CONSTANT,
    RUBBISH,
    EOF, STRING;

    public enum Operator {
        ADDITION,
        KNOT,
        SUBTRACTION,
        KNOT_EQUALS,
        MULTIPLICATION,
        DIVISION,
        EQUALS_EQUALS
    }
}


