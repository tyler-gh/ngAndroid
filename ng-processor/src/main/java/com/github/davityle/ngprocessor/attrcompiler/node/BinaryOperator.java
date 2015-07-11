
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class BinaryOperator extends Expression {

    private final Expression lhs;
    private final Expression rhs;

    public BinaryOperator(Token operator, Expression lhs, Expression rhs) {
        super(operator);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Expression getLHS() {
        return lhs;
    }

    public Expression getRHS() {
        return rhs;
    }
}