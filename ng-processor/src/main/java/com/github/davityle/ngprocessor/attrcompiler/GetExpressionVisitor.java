package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.*;
import com.github.davityle.ngprocessor.util.source.SourceField;

public class GetExpressionVisitor extends AVisitor {
    private StringBuilder result;

    private GetExpressionVisitor() {
        result = new StringBuilder();
    }

    public static String generateGetExpression(Node node) {
        GetExpressionVisitor visitor = new GetExpressionVisitor();
        node.accept(visitor);
        return visitor.result.toString();
    }

    @Override
    public void visit(Node node) {
        result.append(node.getToken().getScript());
    }

    @Override
    public void visit(BinaryOperator node) {
        result.append('(');
        visit(node.getLHS());
        result.append(node.getToken().getScript());
        visit(node.getRHS());
        result.append((')'));
    }

    @Override
    public void visit(ObjectField node) {
        visit(node.getLHS());
        result.append('.');
        result.append("get");
        result.append(SourceField.capitalize(node.getToken().getScript()));
        result.append("()");
    }

    @Override
    public void visit(FunctionCall node) {
        visit(node.getLHS());
        result.append('(');

        boolean isFirst = true;

        for (Expression parameter : node.getParameters()) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(',');
            }

            visit(parameter);
        }

        result.append(')');
    }

    @Override
    public void visit(Identifier node) {
        result.append("scope.");
        result.append(node.getToken().getScript());
    }

    @Override
    public void visit(TernaryOperator node) {
        result.append('(');
        visit(node.getCondition());
        result.append(')');
        result.append('?');
        result.append('(');
        visit(node.getIfTrue());
        result.append(')');
        result.append(':');
        result.append('(');
        visit(node.getIfFalse());
        result.append(')');
    }

    @Override
    public void visit(UnaryOperator node) {
        result.append(node.getToken().getScript());
        visit(node.getRHS());
    }
}
