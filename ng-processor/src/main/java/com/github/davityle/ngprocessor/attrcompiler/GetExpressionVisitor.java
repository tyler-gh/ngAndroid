package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.*;
import com.github.davityle.ngprocessor.source.SourceField;

public class GetExpressionVisitor extends AVisitor {
    private StringBuilder result;
    private final String prependage;

    GetExpressionVisitor(String prependage) {
        this.result = new StringBuilder();
        this.prependage = prependage;
    }

    public static String generateGetExpression(Node node, String value) {
        GetExpressionVisitor visitor = new GetExpressionVisitor(value);
        node.accept(visitor);
        return visitor.result.toString();
    }

    @Override
    public void visit(Node node) {
        result.append(node.getToken().getScript());
    }

    @Override
    public void visit(StringLiteral node) {
        result.append(node.toJavaString());
    }

    @Override
    public void visit(BinaryOperator node) {
        result.append('(');
        node.getLHS().accept(this);
        result.append(node.getToken().getScript());
        node.getRHS().accept(this);
        result.append(')');
    }

    @Override
    public void visit(ObjectField node) {
        node.getLHS().accept(this);
        result.append('.');
        result.append("get");
        result.append(SourceField.capitalize(node.getToken().getScript()));
        result.append("()");
    }

    public void visit(FunctionName node) {
        node.getLHS().accept(this);
        result.append('.');
        result.append(node.getToken().getScript());
    }

    @Override
    public void visit(FunctionCall node) {
        node.getLHS().accept(this);
        result.append('(');

        boolean isFirst = true;

        for (Expression parameter : node.getParameters()) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(',');
            }

            parameter.accept(this);
        }

        result.append(')');
    }

    @Override
    public void visit(Identifier node) {
        result.append(prependage != null ? prependage : "");
        result.append(node.getToken().getScript());
    }

    @Override
    public void visit(TernaryOperator node) {
        result.append('(');
        node.getCondition().accept(this);
        result.append(')');
        result.append('?');
        result.append('(');
        node.getIfTrue().accept(this);
        result.append(')');
        result.append(':');
        result.append('(');
        node.getIfFalse().accept(this);
        result.append(')');
    }

    @Override
    public void visit(UnaryOperator node) {
        result.append(node.getToken().getScript());
        node.getRHS().accept(this);
    }
}
