package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.node.AVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.BinaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.Expression;
import com.github.davityle.ngprocessor.attrcompiler.node.FunctionCall;
import com.github.davityle.ngprocessor.attrcompiler.node.Identifier;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.node.ObjectField;
import com.github.davityle.ngprocessor.attrcompiler.node.TernaryOperator;
import com.github.davityle.ngprocessor.attrcompiler.node.UnaryOperator;
import com.github.davityle.ngprocessor.util.CollectionUtils;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.PrimitiveUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.ArrayList;
import java.util.Stack;

import javax.lang.model.element.Element;

public class TypeCheckVisitor extends AVisitor {

    private final TypeUtils typeUtils;
    private final ElementUtils elementUtils;
    private final PrimitiveUtils primitiveUtils;
    private final Element element;
    private final Stack<Element> elementStack = new Stack<>();
//    private final StringBuilder result = new StringBuilder();
    private String type = "";


    public TypeCheckVisitor(TypeUtils typeUtils, ElementUtils elementUtils, PrimitiveUtils primitiveUtils, Element element){
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.primitiveUtils = primitiveUtils;
        this.element = element;
        elementStack.push(element);
    }

    public static String getType(Node target, Element element, TypeUtils typeUtils, ElementUtils elementUtils, PrimitiveUtils primitiveUtils) {
        try {
            TypeCheckVisitor visitor = new TypeCheckVisitor(typeUtils, elementUtils, primitiveUtils, element);
            target.accept(visitor);
            return primitiveUtils.getObjectType(visitor.type);
        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void visit(BinaryOperator node) {
        node.getLHS().accept(this);
        node.getRHS().accept(this);
    }

    public void visit(final FunctionCall node)  {
//        Option<Element> match = matchingElement(node.getLHS());
        for(Expression expression : node.getParameters()){
            expression.accept(this);
        }
    }

    public void visit(Identifier node)  {
        Option<Element> match = matchingElement(node);
        if(match.isPresent()) {
            elementStack.push(typeUtils.asTypeElement(match.get().asType()));
        }
    }

    public void visit(ObjectField node)  {

        node.getLHS().accept(this);
        Option<Element> match = matchingElement(node);
        if(match.isPresent()) {
            type = elementUtils.getTypeName(match.get());
        }
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

    public Option<Element> matchingElement(final Node node) {
        return CollectionUtils.cl().find(new ArrayList<>(elementStack.peek().getEnclosedElements()), new CollectionUtils.Function<Element, Boolean>() {
            @Override
            public Boolean apply(Element o) {
                return o.getSimpleName().toString().equals(node.getToken().getScript());
            }
        });
    }
    
}
