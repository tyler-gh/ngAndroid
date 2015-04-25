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

package com.github.davityle.ngprocessor.sourcelinks;

import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.source.SourceField;
import com.github.davityle.ngprocessor.util.xml.XmlNode;

import org.apache.velocity.VelocityContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * Created by tyler on 3/30/15.
 */
public class NgScopeSourceLink implements SourceLink{

    private final String className;
    private final String packageName;
    private final List<SourceField> fields;
    private final Element[] elements;
    private final Map<String, Set<XmlNode>> layouts;
    private final String manifestPackageName;
    private List<Source> sources;

    public NgScopeSourceLink(String className, String packageName, List<SourceField> fields, Element[] elements, Map<String, Set<XmlNode>> layouts, String manifestPackageName) {
        this.className = className;
        this.packageName = packageName;
        this.fields = fields;
        this.elements = elements;
        this.layouts = layouts;
        this.manifestPackageName = manifestPackageName;
    }


    public VelocityContext getVelocityContext(){
        VelocityContext vc = new VelocityContext();

        vc.put("simpleClassName", className);
        vc.put("className", packageName + '.' + className);
        vc.put("packageName", packageName);
        vc.put("fields", fields);
        vc.put("layouts", layouts);
        vc.put("manifestPackageName", manifestPackageName);
        return vc;
    }


    @Override
    public Element[] getElements() {
        return elements;
    }

    @Override
    public String getSourceFileName() {
        return packageName + "." + className + NgScopeAnnotationUtils.SCOPE_APPENDAGE;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
