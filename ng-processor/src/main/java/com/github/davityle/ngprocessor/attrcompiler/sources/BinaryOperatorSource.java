/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.davityle.ngprocessor.attrcompiler.sources;

import com.github.davityle.ngprocessor.attrcompiler.parse.TokenType;
import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 2/6/15.
 */
public class BinaryOperatorSource extends Source<BinaryOperatorSource> {

    protected Source leftSide;
    protected Source rightSide;
    protected final TokenType.BinaryOperator operator;

    private BinaryOperatorSource(TypeUtils typeUtils, Source leftSide, Source rightSide, TokenType.BinaryOperator operator, TypeMirror typeMirror) {
        super(typeUtils, typeMirror);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    public static BinaryOperatorSource getOperator(TypeUtils typeUtils, Source leftSide, Source rightSide, TokenType.BinaryOperator operator){
        return new BinaryOperatorSource(typeUtils, leftSide, rightSide, operator, typeUtils.getOperatorKind(leftSide, rightSide, operator));
    }

    @Override
    public String getSource() {
        return '(' + leftSide.getSource() + operator.toString() + rightSide.getSource() + ')';
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        leftSide.getModelSource(models);
        rightSide.getModelSource(models);
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {
        rightSide.getMethodSource(methods);
        leftSide.getMethodSource(methods);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    protected BinaryOperatorSource cp(TypeMirror typeMirror) throws IllegalArgumentException {
        return new BinaryOperatorSource(typeUtils, leftSide.copy(), rightSide.copy(), operator, typeMirror);
    }

    public Source getRightSide() {
        return rightSide;
    }

    public Source getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Source leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(Source rightSide) {
        this.rightSide = rightSide;
    }
}
