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

import com.github.davityle.ngprocessor.SourceField;

import org.apache.velocity.VelocityContext;

import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by tyler on 3/30/15.
 */
public class NgScopeSourceLink implements SourceLink{

    private final String className;
    private final String packageName;
    private final List<SourceField> fields;
    private final Element[] elements;

    public NgScopeSourceLink(String className, String packageName, List<SourceField> fields, Element[] elements) {
        this.className = className;
        this.packageName = packageName;
        this.fields = fields;
        this.elements = elements;
    }


    public VelocityContext getVelocityContext(){
        VelocityContext vc = new VelocityContext();

        vc.put("simpleClassName", className);
        vc.put("className", packageName + '.' + className);
        vc.put("packageName", packageName);
        vc.put("fields", fields);
        return vc;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public Element[] getElements() {
        return elements;
    }
}
