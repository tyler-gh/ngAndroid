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

package com.github.davityle.ngprocessor.map;

import com.github.davityle.ngprocessor.Scope;
import com.github.davityle.ngprocessor.util.CollectionUtils;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.Tuple;
import com.github.davityle.ngprocessor.util.TypeUtils;
import com.github.davityle.ngprocessor.xml.XmlScope;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;

public class LayoutScopeMapper {

    private final Map<String, Collection<XmlScope>> fileNodeMap;
    private final Set<Scope> scopes;

    @Inject
    ElementUtils elementUtils;
    @Inject
    MessageUtils messageUtils;
    @Inject
    TypeUtils typeUtils;
    @Inject
    CollectionUtils collectionUtils;

    public LayoutScopeMapper(Set<Scope> scopes, Map<String, Collection<XmlScope>> fileNodeMap){
        this.scopes = scopes;
        this.fileNodeMap = fileNodeMap;
    }

    public Map<String, Collection<Scope>> mapLayoutsToScopes(){
        return collectionUtils.map(fileNodeMap, new CollectionUtils.Function<Tuple<String, Collection<XmlScope>>, Tuple<String, Collection<Scope>>>() {
            @Override
            public Tuple<String, Collection<Scope>> apply(final Tuple<String, Collection<XmlScope>> layout) {
                return Tuple.of(layout.getFirst(), collectionUtils.filter(scopes, new CollectionUtils.Function<Scope, Boolean>() {
                    @Override
                    public Boolean apply(Scope scope) {
                        Set<Modifier> modifiers = scope.getJavaElement().getModifiers();
                        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                            messageUtils.error(Option.of(scope.getJavaElement()), "Unable to access Scope '%s'. Must have default or public access", scope.toString());
                        }
                        Option<XmlScope> xmlScopeOpt = findScopeInLayout(scope, layout.getSecond());
                        if(xmlScopeOpt.isPresent()) {
                            scope.addXmlScope(layout.getFirst(), xmlScopeOpt.get());
                            return true;
                        } else {
                            return false;
                        }
                    }
                }));
            }
        });
    }

    private Option<XmlScope> findScopeInLayout(Scope scope, Collection<XmlScope> views) {
        for(XmlScope node : views){
            if(scope.getScopeName().equals(node.getScopeName())) {
                return Option.of(node);
            }
        }
        return Option.absent();
    }
}
