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

package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.parse.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by tyler on 2/23/15.
 */
public class PostfixConverter {
    private static boolean lowerPrecedence(TokenType.BinaryOperator op1, TokenType.BinaryOperator op2) {
        switch (op1) {
            case ADDITION:
            case SUBTRACTION:
                return !(op2 == TokenType.BinaryOperator.ADDITION || op2 == TokenType.BinaryOperator.SUBTRACTION) ;
            case MULTIPLICATION:
            case DIVISION:
            default:
                return false;
        }
    }
    public static Object[] convertToPostfix(List<Object> parser) {

        Stack<TokenType.BinaryOperator> operatorStack = new Stack<TokenType.BinaryOperator>();
        List<Object> postfixExpression = new ArrayList<Object>();

        while (parser.size() > 0) {

            Object token = parser.remove(0);
            if (token instanceof TokenType.BinaryOperator) {
                TokenType.BinaryOperator op = (TokenType.BinaryOperator) token;
                while (!operatorStack.empty() && !lowerPrecedence(operatorStack.peek(), op)) {
                    postfixExpression.add(operatorStack.pop());
                }
                operatorStack.push(op);
            }
            else {
                postfixExpression.add(token);
            }
        }

        while (!operatorStack.empty()) {
            postfixExpression.add(operatorStack.pop());
        }

        return postfixExpression.toArray();
    }
}
