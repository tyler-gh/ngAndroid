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

import com.github.davityle.ngprocessor.attrcompiler.Visitors;
import com.github.davityle.ngprocessor.attrcompiler.node.Node;
import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.parse.Parser;
import com.github.davityle.ngprocessor.model.Scope;

public class Source {
    private final Node node;
    private final Visitors visitors;

    public Source(String source, Visitors visitors) throws ParseException {
        this.visitors = visitors;
        this.node = Parser.parse(source);
    }

    public String getGetterSource(String value) {
        return visitors.getGetterSource(node, value);
    }

    public String getSetterSource(String value) {
        return visitors.getSetterSource(node, value);
    }

    public String getObserverSource(String value, String prependage) {
        return visitors.getObserverSource(node, value, prependage);
    }

    public String getType(Scope scope) {
        return visitors.getType(node, scope);
    }

    public boolean isVoid() {
        return false;
    }
}
