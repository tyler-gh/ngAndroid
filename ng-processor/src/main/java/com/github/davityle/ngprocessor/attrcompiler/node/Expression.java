
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public abstract class Expression extends Node {

    protected Expression(Token token) {
        super(token);
    }

    @Override
    public abstract void accept(IVisitor visitor);
}