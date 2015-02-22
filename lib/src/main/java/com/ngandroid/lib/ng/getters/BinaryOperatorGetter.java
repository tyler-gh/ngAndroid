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

package com.ngandroid.lib.ng.getters;

import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.utils.TypeUtils;

/**
 * Created by tyler on 2/6/15.
 */
public abstract class BinaryOperatorGetter implements Getter {

    protected final Getter leftSide;
    protected final Getter rightSide;
    protected final TokenType.BinaryOperator operator;
    protected final int type;

    public BinaryOperatorGetter(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static  BinaryOperatorGetter getOperator(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator){
        switch(operator){
            case ADDITION:
                return new AdditionOperator(leftSide, rightSide, type, operator);
            case SUBTRACTION:
                return new SubtractionOperator(leftSide, rightSide, type, operator);
            case KNOT_EQUALS:
                return new NotEqualOperator(leftSide, rightSide, type, operator);
            case MULTIPLICATION:
                return new MultiplicationOperator(leftSide, rightSide, type, operator);
            case DIVISION:
                return new DivisionOperator(leftSide, rightSide, type, operator);
            case EQUALS_EQUALS:
                return new EqualOperator(leftSide, rightSide, type, operator);
        }
        throw new RuntimeException("No such operator" + operator);
    }


    private static class AdditionOperator extends BinaryOperatorGetter{
        public AdditionOperator(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Object get() throws Throwable {
            switch (type) {
                case TypeUtils.INTEGER:
                    return ((Number)leftSide.get()).intValue() + ((Number)rightSide.get()).intValue();
                case TypeUtils.LONG:
                    return ((Number)leftSide.get()).longValue() + ((Number)rightSide.get()).longValue();
                case TypeUtils.DOUBLE:
                    return ((Number)leftSide.get()).doubleValue() + ((Number)rightSide.get()).doubleValue();
                case TypeUtils.FLOAT:
                    return ((Number)leftSide.get()).floatValue() + ((Number)rightSide.get()).floatValue();
//                case TypeUtils.SHORT:
//                    return (Short)leftSide.get() + (Short)rightSide.get();
//                case TypeUtils.BYTE:
//                    return(Byte)leftSide.get() + (Byte)rightSide.get();
                case TypeUtils.BOOLEAN:
                    throw new RuntimeException("You cannot add a boolean");
                case TypeUtils.STRING:
                    return String.valueOf(leftSide.get()) + String.valueOf(rightSide.get());
                case TypeUtils.OBJECT:
                default:
                    throw new RuntimeException("You cannot add an Object");
            }
        }
    }

    private static class SubtractionOperator extends BinaryOperatorGetter{
        public SubtractionOperator(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Object get() throws Throwable {
            switch (type) {
                case TypeUtils.INTEGER:
                    return ((Number)leftSide.get()).intValue() - ((Number)rightSide.get()).intValue();
                case TypeUtils.LONG:
                    return ((Number)leftSide.get()).longValue() - ((Number)rightSide.get()).longValue();
                case TypeUtils.DOUBLE:
                    return ((Number)leftSide.get()).doubleValue() - ((Number)rightSide.get()).doubleValue();
                case TypeUtils.FLOAT:
                    return ((Number)leftSide.get()).floatValue() - ((Number)rightSide.get()).floatValue();
//                case TypeUtils.SHORT:
//                    return (Short)leftSide.get() - (Short)rightSide.get();
//                case TypeUtils.BYTE:
//                    return(Byte)leftSide.get() - (Byte)rightSide.get();
                case TypeUtils.BOOLEAN:
                    throw new RuntimeException("You cannot subtract a boolean");
                case TypeUtils.STRING:
                    throw new RuntimeException("You cannot subtract a String");
                case TypeUtils.OBJECT:
                default:
                    throw new RuntimeException("You cannot subtract an Object");
            }
        }
    }

    private static class NotEqualOperator extends EqualOperator{
        public NotEqualOperator(Getter<Boolean> leftSide, Getter<Boolean> rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Boolean get() throws Throwable {
            return !super.get();
        }
    }

    private static class EqualOperator extends BinaryOperatorGetter{
        public EqualOperator(Getter<Boolean> leftSide, Getter<Boolean> rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Boolean get() throws Throwable {
            Object leftValue = leftSide.get();
            if(leftValue == null)
                return rightSide.get() == null;
            return leftValue.equals(rightSide.get());
        }
    }

    private static class MultiplicationOperator extends BinaryOperatorGetter{
        public MultiplicationOperator(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Object get() throws Throwable {
            switch (type) {
                case TypeUtils.INTEGER:
                    return ((Number)leftSide.get()).intValue() * ((Number)rightSide.get()).intValue();
                case TypeUtils.LONG:
                    return ((Number)leftSide.get()).longValue() * ((Number)rightSide.get()).longValue();
                case TypeUtils.DOUBLE:
                    return ((Number)leftSide.get()).doubleValue() * ((Number)rightSide.get()).doubleValue();
                case TypeUtils.FLOAT:
                    return ((Number)leftSide.get()).floatValue() * ((Number)rightSide.get()).floatValue();
//                case TypeUtils.SHORT:
//                    return (Short)leftSide.get() * (Short)rightSide.get();
//                case TypeUtils.BYTE:
//                    return(Byte)leftSide.get() * (Byte)rightSide.get();
                case TypeUtils.BOOLEAN:
                    throw new RuntimeException("You cannot multiply a boolean");
                case TypeUtils.STRING:
                    throw new RuntimeException("You cannot multiply a String");
                case TypeUtils.OBJECT:
                default:
                    throw new RuntimeException("You cannot multiply an Object");
            }
        }
    }

    private static class DivisionOperator extends BinaryOperatorGetter{
        public DivisionOperator(Getter leftSide, Getter rightSide, int type, TokenType.BinaryOperator operator) {
            super(leftSide, rightSide, type, operator);
        }

        @Override
        public Object get() throws Throwable {
            switch (type) {
                case TypeUtils.INTEGER:
                    return ((Number)leftSide.get()).intValue() / ((Number)rightSide.get()).intValue();
                case TypeUtils.LONG:
                    return ((Number)leftSide.get()).longValue() / ((Number)rightSide.get()).longValue();
                case TypeUtils.DOUBLE:
                    return ((Number)leftSide.get()).doubleValue() / ((Number)rightSide.get()).doubleValue();
                case TypeUtils.FLOAT:
                    return ((Number)leftSide.get()).floatValue() / ((Number)rightSide.get()).floatValue();
//                case TypeUtils.SHORT:
//                    return (Short)leftSide.get() / (Short)rightSide.get();
//                case TypeUtils.BYTE:
//                    return(Byte)leftSide.get() / (Byte)rightSide.get();
                case TypeUtils.BOOLEAN:
                    throw new RuntimeException("You cannot divide a boolean");
                case TypeUtils.STRING:
                    throw new RuntimeException("You cannot divide a String");
                case TypeUtils.OBJECT:
                default:
                    throw new RuntimeException("You cannot divide an Object");
            }
        }
    }

    public Getter getLeftSide(){
        return leftSide;
    }

    public Getter getRightSide(){
        return rightSide;
    }
}
