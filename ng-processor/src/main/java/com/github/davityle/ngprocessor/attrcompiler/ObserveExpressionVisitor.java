package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.AVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.BinaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.Expression;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionCall;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionName;
import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;
import com.github.davityle.ngprocessor.attrcompiler.node.TernaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.UnaryOperator;

public class ObserveExpressionVisitor extends AVisitor {

    private final String value, prependage;
    private final StringBuilder result = new StringBuilder();

    ObserveExpressionVisitor(String value, String prependage) {
        this.value = value;
        this.prependage = prependage;
    }

    public static String generateObserveExpression(Node target, String value, String prependage) {
        ObserveExpressionVisitor visitor = new ObserveExpressionVisitor(value, prependage);
        target.accept(visitor);
        return visitor.result.toString();
    }

    @Override
    public void visit(Node node) {}

    @Override
    public void visit(Identifier node) {
        if(result.length() > 0) {
            result.append(";\n");
        }
        result.append(prependage != null ? prependage : "");
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

    public void visit(FunctionName node) {
        node.getLHS().accept(this);
    }

    @Override
    public void visit(TernaryOperator node) {
        node.getCondition().accept(this);
        node.getIfTrue().accept(this);
        node.getIfFalse().accept(this);
    }

    @Override
    public void visit(UnaryOperator node) {
        node.getRHS().accept(this);
    }

    @Override
    public void visit(FunctionCall node) {
        node.getLHS().accept(this);
        for (Expression parameter : node.getParameters()) {
            parameter.accept(this);
        }
    }

    @Override
    public void visit(BinaryOperator node) {
        node.getLHS().accept(this);
        node.getRHS().accept(this);
    }
}
