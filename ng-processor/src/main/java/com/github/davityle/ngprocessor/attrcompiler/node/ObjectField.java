
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class ObjectField extends Expression {

    private final Expression lhs;

    public ObjectField(Expression lhs, Token rhs) {
        super(rhs);
        this.lhs = lhs;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Expression getLHS() {
        return lhs;
    }
}