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

import com.github.davityle.ngprocessor.attrcompiler.GetExpressionVisitor;
import com.github.davityle.ngprocessor.attrcompiler.SetExpressionVisitor;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.parse.Parser;

/**
 * Created by davityle on 1/24/15.
 */
public class Source {
    private final Node node;

    public Source(String source) throws ParseException {
        this.node = Parser.parse(source);
    }

    public String getSetterSource() {
        return GetExpressionVisitor.generateGetExpression(node);
    }

    public String getSetterSource(String value) {
        return SetExpressionVisitor.generateSetExpression(node, value);
    }

    public boolean isVoid() {
        return false;
    }
}
