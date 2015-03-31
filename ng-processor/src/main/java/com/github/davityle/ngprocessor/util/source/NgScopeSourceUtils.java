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

package com.github.davityle.ngprocessor.util.source;

import com.github.davityle.ngprocessor.SourceField;
import com.github.davityle.ngprocessor.sourcelinks.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.ElementUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 3/30/15.
 */
public class NgScopeSourceUtils {

    public static List<NgScopeSourceLink> getSourceLinks(Map<String, List<Element>> scopeBuilderMap){
        Set<Map.Entry<String, List<Element>>> entries = scopeBuilderMap.entrySet();
        List<NgScopeSourceLink> scopeSourceLinks = new ArrayList<>();
        for(Map.Entry<String, List<Element>> entry : entries){
            List<Element> elements = entry.getValue();
            scopeSourceLinks.add(NgScopeSourceUtils.getSourceLink(elements));
        }
        return scopeSourceLinks;
    }

    private static NgScopeSourceLink getSourceLink(List<Element> elements){
        Element scopeClass = elements.get(0).getEnclosingElement();
        String packageName = ElementUtils.getPackageName((TypeElement) scopeClass);
        String className = ElementUtils.getClassName((TypeElement) scopeClass, packageName);

        List<SourceField> fields = new ArrayList<>();
        for(Element element : elements){
            Name fieldName = element.getSimpleName();
            TypeMirror fieldType = element.asType();
            fields.add(new SourceField(fieldName.toString(), fieldType.toString()));
        }

        Element[] els = elements.toArray(new Element[elements.size() + 1]);
        els[elements.size()] = scopeClass;

        return new NgScopeSourceLink(className, packageName, fields, els);
    }
}
