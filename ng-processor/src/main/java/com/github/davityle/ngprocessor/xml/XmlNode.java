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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tyler on 3/25/15.
 */
public class XmlNode {
    private final String id;
    private final List<? extends XmlAttribute> attributes;
    private final String elementType;

    public XmlNode(String id, List<? extends XmlAttribute> attributes, String elementType) {
        this.id = id;
        this.attributes = attributes;
        this.elementType = elementType;
    }

    public String getElementType(){
        return elementType;
    }

    public List<? extends XmlAttribute> getAttributes() {
        return attributes;
    }

    public String getId() {
        return id;
    }

    public Set<String> getAttrs() {
        Set<String> dependencies = new HashSet<>();

        for(XmlAttribute attribute : attributes) {
            dependencies.add(attribute.getName());
        }

        return dependencies;
    }

    @Override
    public String toString() {
        return id + ':' + attributes.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 7 + attributes.hashCode() * 31 + elementType.hashCode() * 17;
    }
}

