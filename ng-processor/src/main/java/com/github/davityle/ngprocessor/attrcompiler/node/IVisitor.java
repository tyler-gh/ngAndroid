
package com.github.davityle.ngprocessor.attrcompiler.node;

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
    void visit(FunctionName node);

    /** xml values **/
    void visit(XmlValue node);
    void visit(XmlValue.BoolXmlValue node);
    void visit(XmlValue.ColorXmlValue node);
    void visit(XmlValue.DimenXmlValue node);
    void visit(XmlValue.IdXmlValue node);
    void visit(XmlValue.IntegerXmlValue node);
    void visit(XmlValue.StringXmlValue node);
    void visit(XmlValueKey node);

    /** special identifiers **/
    void visit(SpecialIdentifier node);
    void visit(ViewIdentifier node);
}