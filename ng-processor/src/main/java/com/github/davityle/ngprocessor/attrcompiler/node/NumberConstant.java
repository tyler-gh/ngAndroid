
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class NumberConstant extends Expression {
    public NumberConstant(Token source) {
        super(source);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}