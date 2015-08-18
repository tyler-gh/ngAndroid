
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class UnaryOperator extends Expression {

    private Expression rhs;

    public UnaryOperator(Token token, Expression rhs) {
        super(token);
        this.rhs = rhs;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Expression getRHS() {
        return rhs;
    }
}