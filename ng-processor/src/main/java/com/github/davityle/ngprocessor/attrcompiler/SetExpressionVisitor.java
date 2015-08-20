package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.AVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;
import com.github.davityle.ngprocessor.source.SourceField;

public class SetExpressionVisitor extends AVisitor {

    private final String value;
    private final StringBuilder result = new StringBuilder();

    SetExpressionVisitor(String value) {
        this.value = value;
    }

    public static String generateSetExpression(Node target, String value) {
        SetExpressionVisitor visitor = new SetExpressionVisitor(value);
        target.accept(visitor);
        return visitor.result.toString();
    }

    @Override
    public void visit(ObjectField node) {
        result.append(GetExpressionVisitor.generateGetExpression(node.getLHS(), "", ""));
        result.append('.');
        result.append("set");
        result.append(SourceField.capitalize(node.getToken().getScript()));
        result.append('(');
        result.append(value);
        result.append(')');
    }

    @Override
    public void visit(Identifier node) {
//        result.append(GetExpressionVisitor.generateGetExpression(node));
        result.append("set");
        result.append(node.getToken().getScript());
        result.append("(");
        result.append(value);
        result.append(")");
    }
}
