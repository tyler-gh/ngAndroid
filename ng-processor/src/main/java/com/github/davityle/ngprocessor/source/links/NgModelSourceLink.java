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

package com.github.davityle.ngprocessor.source.links;

import com.github.davityle.ngprocessor.source.SourceField;
import com.github.davityle.ngprocessor.map.ModelScopeMapper;

import org.apache.velocity.VelocityContext;

import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by tyler on 3/30/15.
 */
public class NgModelSourceLink implements SourceLink {

    private final String modelName;
    private final String packageName;
    private final String fullName;
    private final boolean isInterface;
    private final List<SourceField> fields;
    private final Element element;

    public NgModelSourceLink(String modelName, String packageName, String fullName, boolean isInterface, List<SourceField> fields, Element element) {
        this.modelName = modelName;
        this.packageName = packageName;
        this.fullName = fullName;
        this.isInterface = isInterface;
        this.fields = fields;
        this.element = element;
    }

    public VelocityContext getVelocityContext(){
        VelocityContext vc = new VelocityContext();

        vc.put("simpleClassName", modelName);
        vc.put("className", packageName + '.' + modelName);
        vc.put("packageName", packageName);
        vc.put("fullName", fullName);
        vc.put("isInterface", isInterface);
        vc.put("fields", fields);
        return vc;
    }

    @Override
    public Element[] getElements() {
        return new Element[]{element};
    }

    @Override
    public String getSourceFileName() {
        return packageName + "." + modelName + ModelScopeMapper.MODEL_APPENDAGE;
    }
}
