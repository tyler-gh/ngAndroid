package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;

public class ObserveExpressionVisitor extends GetExpressionVisitor {

    private final String value;

    ObserveExpressionVisitor(String value) {
        this.value = value;
    }

    public static String generateObserveExpression(Node target, String value) {
        ObserveExpressionVisitor visitor = new ObserveExpressionVisitor(value);
        target.accept(visitor);
        return visitor.result.toString();
    }

    @Override
    public void visit(Identifier node) {
        result.append("get");
        result.append(node.getToken().getScript());
        result.append("()");
    }

    @Override
    public void visit(ObjectField node) {
        node.getLHS().accept(this);
        result.append('.');
        result.append("add");
        result.append(node.getToken().getScript());
        result.append("Observer(");
        result.append(value);
        result.append(")");
    }
}
