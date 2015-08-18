
package com.github.davityle.ngprocessor.attrcompiler.parse;

public class ParseException extends Exception {
    public ParseException(Token token) {
        super(token.toString());
    }

    public ParseException(Token token, String message) {
        super(token.toString() + "\n" + message);
    }
}