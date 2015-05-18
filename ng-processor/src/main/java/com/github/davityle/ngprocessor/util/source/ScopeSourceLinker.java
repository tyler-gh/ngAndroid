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

import com.github.davityle.ngprocessor.sourcelinks.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.NgScopeAnnotationUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;
import com.github.davityle.ngprocessor.util.xml.XmlNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 3/30/15.
 */
public class ScopeSourceLinker {

    private static final TupleComparator TUPLE_COMPARATOR = new TupleComparator();

    private final List<Element> scopes;
    private final Map<String, List<Element>> scopeMap;
    private final Map<Element, List<XmlNode>> elementNodeMap;
    private final String manifestPackageName;

    public ScopeSourceLinker(List<Element> scopes, Map<String, List<Element>> scopeMap, Map<Element, List<XmlNode>> elementNodeMap, String manifestPackageName) {
        this.scopes = scopes;
        this.scopeMap = scopeMap;
        this.elementNodeMap = elementNodeMap;
        this.manifestPackageName = manifestPackageName;
    }

    public List<NgScopeSourceLink> getSourceLinks(){

        List<NgScopeSourceLink> scopeSourceLinks = new ArrayList<NgScopeSourceLink>();

        for(Element scope : scopes){
            scopeSourceLinks.add(getSourceLink(scope));
        }

        return scopeSourceLinks;
    }

    private NgScopeSourceLink getSourceLink(Element scopeClass){

        TypeElement scopeType = (TypeElement) scopeClass;
        String packageName = ElementUtils.getPackageName(scopeType);
        String className = ElementUtils.getClassName(scopeType, packageName);
        String fullName = ElementUtils.getFullName(scopeType);
        String scopeName = className + NgScopeAnnotationUtils.SCOPE_APPENDAGE;
        String key = packageName + "." + scopeName;

        List<Element> elements = scopeMap.get(key);

        List<SourceField> fields = new ArrayList<SourceField>();
        for(Element element : elements){
            Name fieldName = element.getSimpleName();
            TypeMirror fieldType = element.asType();
            TypeElement typeElement = TypeUtils.asTypeElement(fieldType);
            String pack = ElementUtils.getPackageName(typeElement);
            String modelName = ElementUtils.stripClassName(fieldType);
            fields.add(new SourceField(fieldName.toString(), pack + '.' + modelName));
        }

        Element[] els = elements.toArray(new Element[elements.size() + 1]);
        els[elements.size()] = scopeClass;

        List<XmlNode> xmlNodes = elementNodeMap.get(scopeClass);
        Map<String, Set<XmlNode>> layouts = new HashMap<String, Set<XmlNode>>();

        if(xmlNodes != null) {
            // This could be done by just mapping elements to the layout instead of linking to the
            // node in LayoutScopeMapper
            for (XmlNode xmlNode : xmlNodes) {
                String layoutName = xmlNode.getLayoutName();
                Set<XmlNode> ids = layouts.get(layoutName);
                if (ids == null) {
                    ids = new TreeSet<XmlNode>(TUPLE_COMPARATOR);
                    layouts.put(layoutName, ids);
                }
                ids.add(xmlNode);
            }
        }
        return new NgScopeSourceLink(className, packageName, fullName, fields, els, layouts, manifestPackageName);
    }

}

class TupleComparator implements Comparator<XmlNode> {
    @Override
    public int compare(XmlNode o1, XmlNode o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
