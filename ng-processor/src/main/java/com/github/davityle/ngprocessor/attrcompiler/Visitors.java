package com.github.davityle.ngprocessor.attrcompiler;


import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.model.Scope;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.PrimitiveUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;

import javax.inject.Inject;

public class Visitors {

    private final TypeUtils typeUtils;
    private final ElementUtils elementUtils;
    private final PrimitiveUtils primitiveUtils;

    @Inject
    public Visitors(TypeUtils typeUtils, ElementUtils elementUtils, PrimitiveUtils primitiveUtils){
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.primitiveUtils = primitiveUtils;
    }

    public String getGetterSource(Node node) {
        return GetExpressionVisitor.generateGetExpression(node);
    }

    public String getSetterSource(Node node, String value) {
        return SetExpressionVisitor.generateSetExpression(node, value);
    }

    public String getObserverSource(Node node, String value) {
        return ObserveExpressionVisitor.generateObserveExpression(node, value);
    }

    public String getType(Node node, Scope scope) {
        return TypeCheckVisitor.getType(node, scope.getJavaElement(), typeUtils, elementUtils, primitiveUtils);
    }



}
