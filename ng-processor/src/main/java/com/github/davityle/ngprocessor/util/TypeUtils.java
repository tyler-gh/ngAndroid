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

package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.attrcompiler.parse.TokenType;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Types;

/**
 * Created by tyler on 3/30/15.
 */
public class TypeUtils {
    private static Types typeUtils;

    public static void setTypeUtils(Types types){
        typeUtils = types;
    }

    public static TypeElement asTypeElement(TypeMirror typeMirror){
        return (TypeElement) typeUtils.asElement(typeMirror);
    }
    public static TypeMirror getOperatorKind(Source leftSide, Source rightSide){
        return getOperatorKind(leftSide, rightSide, null);
    }
    public static TypeMirror getOperatorKind(Source leftSide, Source rightSide, TokenType.BinaryOperator operator){
        return getOperatorKind(leftSide.getTypeMirror(), rightSide.getTypeMirror(), operator);
    }
    public static TypeMirror getOperatorKind(TypeMirror leftMirror, TypeMirror rightMirror){
        return getOperatorKind(leftMirror, rightMirror, null);
    }
    /**
     * rules for this method are found here
     *
     * http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
     *
     * If any operand is of a reference type, it is subjected to unboxing conversion (§5.1.8).
     * Widening primitive conversion (§5.1.2) is applied to convert either or both operands as specified by the following rules:
     * If either operand is of type double, the other is converted to double.
     * Otherwise, if either operand is of type float, the other is converted to float.
     * Otherwise, if either operand is of type long, the other is converted to long.
     * Otherwise, both operands are converted to type int.
     *
     * test this hardcore..
     *
     * @param leftMirror
     * @param rightMirror
     * @param operator
     * @return
     */
    public static TypeMirror getOperatorKind(TypeMirror leftMirror, TypeMirror rightMirror , TokenType.BinaryOperator operator){

        if(leftMirror == null || rightMirror == null)
            return null;

        TypeKind leftKind = leftMirror.getKind();
        TypeKind rightKind = rightMirror.getKind();

        if(isString(leftMirror))
            return leftMirror;
        if(isString(rightMirror))
            return rightMirror;

        if(either(leftKind, rightKind, TypeKind.BOOLEAN) ) {
            if(operator != null) {
                MessageUtils.error(null, "Cannot perform perform the operation '%s' with a boolean type. (%s %s %s)", operator.toString(), leftMirror, operator.toString(), rightMirror);
            }
            return null;
        }
        //TODO check if they are boxed type
        if(leftKind.isPrimitive() && rightKind.isPrimitive()) {
            if (typeUtils.isSameType(leftMirror, rightMirror))
                return typeUtils.getPrimitiveType(leftKind);

            if (either(leftKind, rightKind, TypeKind.DOUBLE))
                return typeUtils.getPrimitiveType(TypeKind.DOUBLE);

            if (either(leftKind, rightKind, TypeKind.FLOAT))
                return typeUtils.getPrimitiveType(TypeKind.FLOAT);

            if (either(leftKind, rightKind, TypeKind.LONG))
                return typeUtils.getPrimitiveType(TypeKind.LONG);

            if (
                    either(leftKind, rightKind, TypeKind.INT) ||
                            either(leftKind, rightKind, TypeKind.CHAR) ||
                            either(leftKind, rightKind, TypeKind.SHORT) ||
                            either(leftKind, rightKind, TypeKind.BYTE)
                    ) {
                return typeUtils.getPrimitiveType(TypeKind.INT);
            }
            // one of the above should always return
        }
        TypeMirror intMirror = typeUtils.getPrimitiveType(TypeKind.INT);
        if(both(assignable(intMirror, leftMirror), assignable(intMirror, rightMirror)))
            return intMirror;

        TypeMirror longMirror = typeUtils.getPrimitiveType(TypeKind.LONG);
        if(both(assignable(longMirror, leftMirror), assignable(longMirror, rightMirror)))
            return longMirror;

        TypeMirror floatMirror = typeUtils.getPrimitiveType(TypeKind.FLOAT);
        if(both(assignable(floatMirror, leftMirror), assignable(floatMirror, rightMirror)))
            return floatMirror;

        TypeMirror doubleMirror = typeUtils.getPrimitiveType(TypeKind.DOUBLE);
        if(both(assignable(doubleMirror, leftMirror), assignable(doubleMirror, rightMirror)))
            return doubleMirror;

        return null;
    }

    private static boolean both(boolean a, boolean b){
        return a && b;
    }

    public static boolean assignable(TypeMirror current, TypeMirror typeMirror){
        return typeUtils.isSubtype(typeMirror, current)  ||
                typeUtils.isAssignable(typeMirror, current);
    }

    public static boolean matchFirstPrecedence(TypeMirror current, TypeMirror typeMirror) {
        if(isString(current) && isString(typeMirror))
            return true;

        return typeUtils.isSameType(current, typeMirror) ||
                typeUtils.isSubtype(typeMirror, current)  ||
                typeUtils.isAssignable(typeMirror, current);
    }

    public static boolean isSameType(TypeMirror current, TypeMirror typeMirror){
        if(isString(current) && isString(typeMirror))
            return true;

        return typeUtils.isSameType(current, typeMirror);
    }

    public static boolean match(TypeMirror current, TypeMirror typeMirror) {
        if(isString(current) && isString(typeMirror))
            return true;

        return typeUtils.isSameType(current, typeMirror) ||
                typeUtils.isSubtype(current, typeMirror)  ||
                typeUtils.isSubtype(typeMirror, current)  ||
                typeUtils.isAssignable(current, typeMirror) ||
                typeUtils.isAssignable(typeMirror, current);
    }

    private static boolean either(TypeKind left, TypeKind right, TypeKind compare){
        return left.equals(compare) || right.equals(compare);
    }


    public static boolean isString(TypeMirror typeMirror){
        return String.class.getName().equals(typeMirror.toString());
    }

    public static TypeMirror getLongType() {
        return typeUtils.getPrimitiveType(TypeKind.LONG);
    }

    public static TypeMirror getDoubleType() {
        return typeUtils.getPrimitiveType(TypeKind.DOUBLE);
    }

    public static TypeMirror getFloatType() {
        return typeUtils.getPrimitiveType(TypeKind.FLOAT);
    }

    public static TypeMirror getStringType() {
        return STRING;
    }

    public static TypeMirror getIntegerType() {
        return typeUtils.getPrimitiveType(TypeKind.INT);
    }

    public static TypeMirror getBooleanType() {
        return typeUtils.getPrimitiveType(TypeKind.BOOLEAN);
    }

    /**
     * Stub TypeMirror for a String
     */
    private static final TypeMirror STRING = new TypeMirror() {
        public TypeKind getKind() {return null;}
        public <R, P> R accept(TypeVisitor<R, P> v, P p) {return null;}
        public List<? extends AnnotationMirror> getAnnotationMirrors() {return null;}
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {return null;}
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {return null;}

        @Override
        public String toString() {
            return String.class.getName();
        }
    };

    public static boolean equalsOrHasPrecedence(TypeMirror precedence, TypeMirror mirror) {
        return typeUtils.isSameType(precedence, mirror) || typeUtils.isAssignable(mirror, precedence);
    }
}
