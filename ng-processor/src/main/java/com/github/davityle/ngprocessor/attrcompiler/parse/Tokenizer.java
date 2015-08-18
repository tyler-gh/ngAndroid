
package com.github.davityle.ngprocessor.attrcompiler.parse;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private String source;
    private int currentTokenStart;
    private int currentPosition;
    private State state;

    public Tokenizer(String source) {
        this.source = source;
        this.currentTokenStart = 0;
        this.currentPosition = 0;
        this.state = State.StartState;
    }

    public List<Token> tokenize() throws ParseException {
        List<Token> result = new ArrayList<Token>();

        if (source != null && source.length() != 0) {
            State currentState = State.StartState;

            while (currentState != State.Done && currentPosition <= source.length()) {
                char currentChar;

                if (currentPosition == source.length()) {
                    currentChar = '\0';
                } else {
                    currentChar = source.charAt(currentPosition);
                }

                StepResult stepResult = currentState.step(currentChar);
                TokenType tokenType = stepResult.getTokenType();
                currentState = stepResult.getState();

                if (tokenType != TokenType.NONE) {
                    if (tokenType == TokenType.RUBBISH) {
                        String tokenValue = source.substring(currentTokenStart, currentPosition);
                        throw new ParseException(new Token(tokenType, tokenValue, currentTokenStart), "Unexpected character '" + currentChar + "' at col " + currentTokenStart);
                    } else if (tokenType != TokenType.WHITESPACE) {
                        String tokenValue = source.substring(currentTokenStart, currentPosition);
                        result.add(new Token(tokenType, tokenValue, currentTokenStart));
                    }

                    currentTokenStart = currentPosition;
                }

                ++currentPosition;
            }
        }

        result.add(new Token(TokenType.EOF, "", source.length()));
        return result;
    }
}
