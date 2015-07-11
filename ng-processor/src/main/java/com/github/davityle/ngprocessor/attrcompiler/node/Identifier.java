
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class Identifier extends Expression {
    public Identifier(Token identifier) {
        super(identifier);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}