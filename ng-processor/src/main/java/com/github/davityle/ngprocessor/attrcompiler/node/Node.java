
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public abstract class Node {

    private final Token token;

    protected Node(Token token) {
        this.token = token;
    }

    public abstract void accept(IVisitor visitor);

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.toString();
    }
}