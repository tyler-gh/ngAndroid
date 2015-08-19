package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public final class ViewIdentifier extends SpecialIdentifier {
    public ViewIdentifier(Token token) {
        super(token);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getCompiledSource() {
        return "view";
    }
}
