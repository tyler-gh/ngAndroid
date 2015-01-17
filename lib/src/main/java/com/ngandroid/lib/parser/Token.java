package com.ngandroid.lib.parser;

/**
* Created by davityle on 1/15/15.
*/
public final class Token {
    private final TokenType tokenType;
    private final String script;

    Token(TokenType tokenType, String script) {
        this.tokenType = tokenType;
        this.script = script;
    }

    @Override
    public String toString() {
        return tokenType.toString() + "::" + script;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getScript() {
        return script;
    }
}
