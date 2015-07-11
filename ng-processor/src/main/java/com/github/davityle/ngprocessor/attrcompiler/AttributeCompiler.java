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


import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.parse.Parser;

import javax.inject.Inject;

public class AttributeCompiler {


    @Inject
    public AttributeCompiler(){}

    public static String translateToJavaGetter(String source) {
        Node expression = Parser.tryParse(source);

        if (expression == null) {
            return null;
        } else {
            return GetExpressionVisitor.generateGetExpression(expression);
        }
    }

    public static String translateToJavaSetter(String source, String value) {
        Node expression = Parser.tryParse(source);

        if (expression == null) {
            return null;
        } else {
            return SetExpressionVisitor.generateSetExpression(expression, value);
        }
    }
}
