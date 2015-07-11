
package com.github.davityle.ngprocessor.attrcompiler.node;

import org.apache.velocity.util.TemplateNumber;

public interface IVisitor {
    void visit(Node node);
    void visit(Expression node);
    void visit(BinaryOperator node);
    void visit(FunctionCall node);
    void visit(Identifier node);
    void visit(NumberConstant node);
    void visit(StringLiteral node);
    void visit(ObjectField node);
    void visit(TernaryOperator node);
    void visit(UnaryOperator node);
}