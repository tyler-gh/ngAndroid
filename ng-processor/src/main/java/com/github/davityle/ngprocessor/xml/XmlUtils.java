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
import com.github.davityle.ngprocessor.attributes.Attributes;
import com.github.davityle.ngprocessor.attributes.ScopeAttrNameResolver;
import com.github.davityle.ngprocessor.finders.LayoutsFinder;
import com.github.davityle.ngprocessor.finders.NamespaceFinder;
import com.github.davityle.ngprocessor.util.CollectionUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.Option;
import com.github.davityle.ngprocessor.util.Tuple;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlUtils {

    private static final Pattern ID_ATTR_PATTERN = Pattern.compile("android:id=\"@\\+id/(.+)\"");
    private static final String NAMESPACE_ATTRIBUTE_REG = "%s:(.+)=\"(.+)\"";

    private final MessageUtils messageUtils;
    private final CollectionUtils collectionUtils;
    private final ScopeAttrNameResolver scopeAttrNameResolver;
    private final LayoutsFinder layoutsFinder;
    private final NamespaceFinder namespaceFinder;
    private final Attributes attributes;
    private Pattern attrPattern;

    @Inject
    public XmlUtils(MessageUtils messageUtils, CollectionUtils collectionUtils, ScopeAttrNameResolver scopeAttrNameResolver, LayoutsFinder layoutsFinder, NamespaceFinder namespaceFinder, Attributes attributes){
        this.messageUtils = messageUtils;
        this.collectionUtils = collectionUtils;
        this.scopeAttrNameResolver = scopeAttrNameResolver;
        this.layoutsFinder = layoutsFinder;
        this.namespaceFinder = namespaceFinder;
        this.attributes = attributes;
    }

    /**
     * maps all of the layouts to their scopes, could be optimized
     * @return
     */
    public Map<String, Collection<XmlScope>> getXmlScopes() {
        List<File> layoutDirs = layoutsFinder.findLayoutDirs();

        Collection<File> layoutFiles = collectionUtils.flatMap(layoutDirs, new CollectionUtils.Function<File, Collection<File>>() {
            @Override
            public Collection<File> apply(File file) {
                return collectionUtils.filter(Arrays.asList(file.listFiles()), new CollectionUtils.Function<File, Boolean>() {
                    @Override
                    public Boolean apply(File file) {
                        return file.getName().endsWith(".xml");
                    }
                });
            }
        });

        Collection<Document> docs = collectionUtils.flatMapOpt(layoutFiles, new CollectionUtils.Function<File, Option<Document>>() {
            @Override
            public Option<Document> apply(File file) {
                return getDocumentFromFile(file);
            }
        });

        return collectionUtils.toMap(collectionUtils.flatMapOpt(docs, new CollectionUtils.Function<Document, Option<Tuple<String, Collection<XmlScope>>>>() {
            @Override
            public Option<Tuple<String, Collection<XmlScope>>> apply(Document doc) {
                Option<String> optNameSpace = namespaceFinder.getNameSpace(doc);
                if (optNameSpace.isPresent()) {
                    attrPattern = Pattern.compile(String.format(NAMESPACE_ATTRIBUTE_REG, optNameSpace.get()));
                    Collection<XmlScope> scopes = getScopes(doc);
                    return Option.of(Tuple.of(doc.getDocumentURI(), scopes));
                } else {
                    return Option.absent();
                }
            }
        }));
    }

    private Collection<XmlScope> getScopes(Node scopeNode) {

        NodeListCollection nodeList = new NodeListCollection(scopeNode.getChildNodes());

        Collection<XmlScope> scopes = collectionUtils.flatMapOpt(nodeList, new CollectionUtils.Function<Node, Option<XmlScope>>() {
            @Override
            public Option<XmlScope> apply(final Node node) {
                return getScopeAttr(node).map(new Option.Map<XmlScope, XmlScope>() {
                    @Override
                    public XmlScope map(XmlScope xmlScope) {
                        return xmlScope.addViews(collectionUtils.flatMap(new NodeListCollection(node.getChildNodes()), new CollectionUtils.Function<Node, Collection<XmlView>>() {
                            @Override
                            public Collection<XmlView> apply(Node node) {
                                return getAttributes(node);
                            }
                        }));
                    }
                });
            }
        });

        scopes.addAll(collectionUtils.flatMap(nodeList, new CollectionUtils.Function<Node, Collection<XmlScope>>() {
            @Override
            public Collection<XmlScope> apply(Node node) {
                return getScopes(node);
            }
        }));

        return scopes;
    }

    private Option<XmlScope> getScopeAttr(Node node) {
        return Option.of(node.getAttributes()).fold(new Option.OptionCB<NamedNodeMap, Option<XmlScope>>() {
            @Override
            public Option<XmlScope> absent() {
                return Option.absent();
            }
            @Override
            public Option<XmlScope> present(NamedNodeMap namedNodeMap) {
                for (Node attr : new NamedNodeMapCollection(namedNodeMap)) {
                    Matcher matcher = attrPattern.matcher(attr.toString());
                    if(matcher.matches() && scopeAttrNameResolver.getScopeAttrName().equals(matcher.group(1))) {
                        return Option.of(new XmlScope(matcher.group(2)));
                    }
                }
                return Option.absent();
            }
        });
    }

    private Collection<XmlView> getAttributes(final Node node) {
        // TODO add sub scopes
        if(getScopeAttr(node).isPresent())
            return Collections.emptyList();

        final Option<String> nodeId = Option.of(node.getAttributes()).map(new Option.Map<NamedNodeMap, String>() {
            @Override
            public String map(NamedNodeMap namedNodeMap) {
                return getNodeId(namedNodeMap).getOrElse(null);
            }
        });

        final Collection<XmlAttribute> nodeAttrs = Option.of(node.getAttributes()).fold(new Option.OptionCB<NamedNodeMap, Collection<XmlAttribute>>() {
            @Override
            public Collection<XmlAttribute> absent() {
                return Collections.emptyList();
            }

            @Override
            public Collection<XmlAttribute> present(NamedNodeMap namedNodeMap) {
                List<XmlAttribute> attributeList = new ArrayList<>();
                for (Node node : new NamedNodeMapCollection(namedNodeMap)) {
                    Matcher matcher = attrPattern.matcher(node.toString());
                    if(matcher.matches() && attributes.containsKey(matcher.group(1))) {
                        String attr = matcher.group(1);
                        String value = matcher.group(2);
                        try {
                            XmlAttribute xmlAttribute = new XmlAttribute(attr, value, nodeId);
                            attributeList.add(xmlAttribute);
                        } catch (ParseException | RuntimeException e) {
                            Object[] params = new Object[]{node.getBaseURI(), attr, nodeId.getOrElse("no id available"), value, e.getMessage()};
                            messageUtils.error(Option.<Element>absent(), "Layout file '%s' has an invalid attribute '%s' in view '%s' with value '%s' because '%s'", params);
                        }
                    }
                }

                return attributeList;
            }
        });
        Collection<XmlView> views = new ArrayList<>();
        if(!nodeAttrs.isEmpty()) {
            Option<XmlView> view = nodeId.fold(new Option.OptionCB<String, Option<XmlView>>() {
                @Override
                public Option<XmlView> absent() {
                    messageUtils.error(Option.<Element>absent(), "View '%s' must have an id in order for ngAndroid to use its attributes.", node);
                    return Option.absent();
                }

                @Override
                public Option<XmlView> present(String s) {
                    return Option.of(new XmlView(s, nodeAttrs, node.getNodeName()));
                }
            });
            if(view.isPresent()) {
                views.add(view.get());
            }
        }
        views.addAll(collectionUtils.flatMap(new NodeListCollection(node.getChildNodes()), new CollectionUtils.Function<Node, Collection<XmlView>>() {
            @Override
            public Collection<XmlView> apply(Node node) {
                return getAttributes(node);
            }
        }));

        return views;
    }

    private Option<String> getNodeId(NamedNodeMap nodeMap) {
        for(int j = 0; j < nodeMap.getLength(); j++) {
            Node node = nodeMap.item(j);
            Matcher idMatcher = ID_ATTR_PATTERN.matcher(node.toString());

            if (idMatcher.matches()) {
                return Option.of(idMatcher.group(1));
            }
        }
        return Option.absent();
    }

    public Option<Document> getDocumentFromFile(File file){
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.setDocumentURI(file.getAbsolutePath());
            return Option.of(document);
        } catch (Exception e) {
            messageUtils.error(Option.<Element>absent(), e.getMessage());
            return Option.absent();
        }
    }
}
