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

package com.github.davityle.ngprocessor.xml;

import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.attributes.Attribute;
import com.github.davityle.ngprocessor.util.Option;

/**
* Created by tyler on 3/25/15.
*/
public class XmlAttribute {
    private final Attribute attr;
    private final Option<String> viewId;
    private Source source;

    XmlAttribute(Attribute attr, Source source, Option<String> viewId) throws ParseException {
        this.attr = attr;
        this.viewId = viewId;
        this.source = source;
    }

    public String getId() {
        return viewId.get();
    }

    public String getName() {
        return attr.getAttrName();
    }

    @Override
    public String toString() {
        return '(' + attr.getAttrName() + ',' + source.getGetterSource() + ')';
    }

    public Source getSource() {
        return source;
    }

    public String getClassSource() {
        return attr.getClassSource();
    }

    public String getAttachSource() {
        return attr.getAttachSource();
    }

    public String getClassName() {
        return getId() + getName();
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
