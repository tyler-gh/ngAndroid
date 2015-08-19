package com.github.davityle.ngprocessor.attrcompiler.parse;

public enum State {
    StartState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar));
        }
    },

    NumberInt {
        StepResult step(char currentChar) {
            if (Character.isDigit(currentChar)) {
                return new StepResult(State.NumberInt);
            } else if (currentChar == '.') {
                return new StepResult(State.NumberFraction);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.INT_CONSTANT);
            }
        }
    },

    NumberFraction {
        StepResult step(char currentChar) {
            if (Character.isDigit(currentChar)) {
                return new StepResult(State.NumberFraction);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.DOUBLE_CONSTANT);
            }
        }
    },

    WhitespaceState {
        StepResult step(char currentChar) {
            if (Character.isWhitespace(currentChar)) {
                return new StepResult(State.WhitespaceState);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.WHITESPACE);
            }
        }
    },

    OpClosePState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.CLOSE_PARENTHESIS);
        }
    },

    OpOpenPState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.OPEN_PARENTHESIS);
        }
    },

    OpCommaState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.COMMA);
        }
    },

    OpDotState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.PERIOD);
        }
    },

    OpBinary {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.BINARY_OPERATOR);
        }
    },

    OpLessThan {
        StepResult step(char currentChar) {
            if (currentChar == '=') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.BINARY_OPERATOR);
            }
        }
    },

    OpGreaterThan {
        StepResult step(char currentChar) {
            if (currentChar == '=') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.BINARY_OPERATOR);
            }
        }
    },

    OpEqual {
        StepResult step(char currentChar) {
            if (currentChar == '=') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            }
        }
    },

    OpNot {
        StepResult step(char currentChar) {
            if (currentChar == '=') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.UNARY_OPERATOR);
            }
        }
    },

    OpOr {
        StepResult step(char currentChar) {
            if (currentChar == '|') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            }
        }
    },

    OpAnd {
        StepResult step(char currentChar) {
            if (currentChar == '&') {
                return new StepResult(State.OpBinary);
            } else {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            }
        }
    },

    QuestionMark {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.TERNARY_QUESTION_MARK);
        }
    },

    Colon {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.TERNARY_COLON);
        }
    },

    StringState {
        StepResult step(char currentChar) {
            if (currentChar == '\'') {
                return new StepResult(State.StringEndState);
            } else if (currentChar == '\\') {
                return new StepResult(State.StringEscapeState);
            } else if (currentChar == '\0') {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            } else {
                return new StepResult(State.StringState);
            }
        }
    },

    StringEscapeState {
        StepResult step(char currentChar) {
            if (currentChar == '\0') {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            } else {
                return new StepResult(State.StringState);
            }
        }
    },

    StringEndState {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar), TokenType.STRING);
        }
    },

    IdentifierState {
        StepResult step(char currentChar) {
            if (Character.isLetterOrDigit(currentChar) || currentChar == '_' || currentChar == '$') {
                return new StepResult(State.IdentifierState);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.IDENTIFIER);
            }
        }
    },

    SpecialIdentifierState {
        StepResult step(char currentChar) {
            if (Character.isLetterOrDigit(currentChar) || currentChar == '_' || currentChar == '$') {
                return new StepResult(State.SpecialIdentifierState);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.SPECIAL_IDENTIFIER);
            }
        }
    },

    XmlValueState {
        StepResult step(char currentChar) {
            if (Character.isLetter(currentChar)) {
                return new StepResult(State.XmlValueState);
            } else if(currentChar == '/'){
                return new StepResult(State.XmlValueKeyState, TokenType.XML_VALUE);
            } else {
                return new StepResult(State.ErrorState, TokenType.RUBBISH);
            }
        }
    },

    XmlValueKeyState {
        StepResult step(char currentChar) {
            if (Character.isLetter(currentChar)) {
                return new StepResult(State.XmlValueKeyState);
            } else {
                return new StepResult(DefaultState(currentChar), TokenType.XML_VALUE_KEY);
            }
        }
    },

    ErrorState {
        StepResult step(char currentChar) {
            if (currentChar == '\0') {
                return new StepResult(State.EOFState, TokenType.RUBBISH);
            } else {
                return new StepResult(State.EOFState);
            }
        }
    },

    EOFState {
        StepResult step(char currentChar) {
            return new StepResult(State.EOFState);
        }
    },

    Done {
        StepResult step(char currentChar) {
            return new StepResult(DefaultState(currentChar));
        }
    };

    abstract StepResult step(char currentChar);

    private static State DefaultState(char currentChar) {
        if (Character.isDigit(currentChar)) {
            return State.NumberInt;
        } else if (Character.isLetter(currentChar)) {
            return State.IdentifierState;
        } else if (Character.isWhitespace(currentChar)) {
            return State.WhitespaceState;
        } else if (currentChar == '$') {
            return State.SpecialIdentifierState;
        } else if (currentChar == '@') {
            return State.XmlValueState;
        } else if (currentChar == '(') {
            return State.OpOpenPState;
        } else if (currentChar == ')') {
            return State.OpClosePState;
        } else if (currentChar == '\'') {
            return State.StringState;
        } else if (currentChar == '.') {
            return State.OpDotState;
        } else if(currentChar == '|') {
            return State.OpOr;
        } else if(currentChar == '&') {
            return State.OpAnd;
        } else if ("+-*/".indexOf(currentChar) != -1) {
            return State.OpBinary;
        } else if (currentChar == '<') {
            return State.OpLessThan;
        } else if (currentChar == '>') {
            return State.OpGreaterThan;
        } else if (currentChar == '=') {
            return State.OpEqual;
        } else if (currentChar == '!') {
            return State.OpNot;
        } else if (currentChar == ',') {
            return State.OpCommaState;
        } else if (currentChar == '?') {
            return State.QuestionMark;
        } else if (currentChar == ':') {
            return State.Colon;
        } else if (currentChar == '\0') {
            return State.EOFState;
        } else {
            return State.ErrorState;
        }
    }
}