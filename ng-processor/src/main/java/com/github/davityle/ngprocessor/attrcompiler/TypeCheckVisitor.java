package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.AVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.BinaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.Expression;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionCall;
import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;
import com.github.davityle.ngprocessor.attrcompiler.node.TernaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.UnaryOperator;

import javax.lang.model.element.Element;

/**
 * Created by tyler on 7/11/15.
 */
public class TypeCheckVisitor extends AVisitor {

    private final Element element;

    public TypeCheckVisitor(Element element){
        this.element = element;
    }

    public void visit(BinaryOperator node) {
        node.getLHS().accept(this);
        node.getRHS().accept(this);
    }

    public void visit(FunctionCall node)  {
        System.out.println(node.getToken().getScript());

        for(Expression expression : node.getParameters()){
            expression.accept(this);
        }
    }

    public void visit(Identifier node)  {
        System.out.println(node.getToken().getScript());
    }

    public void visit(ObjectField node)  {
        System.out.println(node.getToken().getScript());
        node.getLHS().accept(this);
    }

    public void visit(TernaryOperator node)  {
        node.getCondition().accept(this);
        node.getIfTrue().accept(this);
        node.getIfFalse().accept(this);
    }

    public void visit(UnaryOperator node)  {
        node.getRHS().accept(this);
    }

    public enum MatchType {
        MATCH,
        PARTIAL,
        NOT
    }
    
}
