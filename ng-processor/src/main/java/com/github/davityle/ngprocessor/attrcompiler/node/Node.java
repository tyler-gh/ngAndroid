
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class Node {

    private final Token token;

    protected Node(Token token) {
        this.token = token;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Token getToken() {
        return token;
    }
}