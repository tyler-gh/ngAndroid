
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class Expression extends Node {

    protected Expression(Token token) {
        super(token);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}