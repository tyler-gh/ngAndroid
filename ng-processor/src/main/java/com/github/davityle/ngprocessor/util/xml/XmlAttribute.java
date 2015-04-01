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

package com.github.davityle.ngprocessor.util.xml;

import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;

import java.util.ArrayList;
import java.util.List;

/**
* Created by tyler on 3/25/15.
*/
public class XmlAttribute {
    private final String name, value;
    private Source source;

    XmlAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '(' + name + ',' + value + ')';
    }

    public Source getSource() {
        return source;
    }

    public List<ModelSource> getModelSource() {
        List<ModelSource> models = new ArrayList<>();
        source.getModelSource(models);
        return models;
    }

    public List<MethodSource> getMethodSource() {
        List<MethodSource> methods = new ArrayList<>();
        source.getMethodSource(methods);
        return methods;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
