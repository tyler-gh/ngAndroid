package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class XmlValueKey extends Expression{

    public XmlValueKey(Token token) {
        super(token);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
