
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class TernaryOperator extends Expression {

    private final Expression condition;
    private final Expression ifTrue;
    private final Expression ifFalse;

    public TernaryOperator(Token questionMark, Expression condition, Expression ifTrue, Expression ifFalse) {
        super(questionMark);
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getIfTrue() {
        return ifTrue;
    }

    public Expression getIfFalse() {
        return ifFalse;
    }
}