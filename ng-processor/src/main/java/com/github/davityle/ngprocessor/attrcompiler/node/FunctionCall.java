
package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

import java.util.List;

public class FunctionCall extends Expression {

    private final Expression lhs;
    private List<? extends Expression> parameters;

    public FunctionCall(Token token, Expression lhs, List<? extends Expression> parameters) {
        super(token);
        this.lhs = lhs;
        this.parameters = parameters;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public Expression getLHS() {
        return lhs;
    }

    public List<? extends Expression> getParameters() {
        return parameters;
    }
}