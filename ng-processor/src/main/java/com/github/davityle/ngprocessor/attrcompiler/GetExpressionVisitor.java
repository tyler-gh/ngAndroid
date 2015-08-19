package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.AVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.BinaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.Expression;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionCall;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionName;
import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;
import com.github.davityle.ngprocessor.attrcompiler.node.StringLiteral;
import com.github.davityle.ngprocessor.attrcompiler.node.TernaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.UnaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.SpecialIdentifier;
import com.github.davityle.ngprocessor.attrcompiler.node.ViewIdentifier;
import com.github.davityle.ngprocessor.source.SourceField;

import java.util.Collection;
import java.util.function.Consumer;

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
        if(node instanceof SpecialIdentifier) {
            throw new RuntimeException();
        }
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
        parseFunctionParameters(node.getParameters());
        result.append(')');
    }

    private <T extends Expression> void parseFunctionParameters(Collection<T> parameters) {
        parameters.forEach(new Consumer<T>() {
            boolean isFirst = true;

            @Override
            public void accept(T parameter) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    result.append(',');
                }
                parameter.accept(GetExpressionVisitor.this);
            }
        });
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

    @Override
    public void visit(SpecialIdentifier node) {
//        result.append(node.getCompiledSource());
    }

    @Override
    public void visit(ViewIdentifier node) {
        result.append(node.getCompiledSource());
    }
}
