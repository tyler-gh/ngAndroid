
package com.github.davityle.ngprocessor.attrcompiler.node;

public abstract class AVisitor implements IVisitor {
    public void visit(Node node) {
        throw new UnsupportedOperationException("Visitor does not support node " + node.toString());
    }

    public void visit(Expression node) {
        visit((Node)node);
    }

    public void visit(BinaryOperator node) {
        visit((Expression)node);
    }

    public void visit(FunctionCall node)  {
        visit((Expression)node);
    }
    public void visit(Identifier node)  {
        visit((Expression)node);
    }
    public void visit(NumberConstant node)  {
        visit((Expression)node);
    }
    public void visit(StringLiteral node) { visit((Expression)node); }
    public void visit(ObjectField node)  {
        visit((Expression)node);
    }
    public void visit(TernaryOperator node)  {
        visit((Expression)node);
    }
    public void visit(UnaryOperator node)  {
        visit((Expression)node);
    }
    public void visit(FunctionName node) {
        visit((Expression) node);
    }
    public void visit(SpecialIdentifier node) {
        visit((Expression) node);
    }
    public void visit(ViewIdentifier node) {
        visit((Expression) node);
    }
}