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

import com.github.davityle.ngprocessor.attrcompiler.AttributeCompiler;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.attributes.Attributes;
import com.github.davityle.ngprocessor.finders.LayoutsFinder;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.Option;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by tyler on 3/30/15.
 */
public class XmlUtils {

    private static final Pattern NAME_SPACE_PATTERN = Pattern.compile("xmlns:(.+)=\"http://schemas.android.com/apk/res-auto\"");
    private static final Pattern ID_ATTR_PATTERN = Pattern.compile("android:id=\"@\\+id/(.+)\"");
    private static final String NAMESPACE_ATTRIBUTE_REG = "%s:(.+)=\"(.+)\"";

    private final MessageUtils messageUtils;
    private final LayoutsFinder layoutsFinder;
    private final AttributeCompiler attributeCompiler;
    private final Attributes attributes;

    @Inject
    public XmlUtils(MessageUtils messageUtils, LayoutsFinder layoutsFinder, AttributeCompiler attributeCompiler, Attributes attributes){
        this.messageUtils = messageUtils;
        this.layoutsFinder = layoutsFinder;
        this.attributeCompiler = attributeCompiler;
        this.attributes = attributes;
    }

    public Option<Document> getDocumentFromFile(File file){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return Option.of(db.parse(file));
        } catch (Exception e) {
            messageUtils.error(null, e.getMessage());
            return Option.absent();
        }
    }

    public Map<File, List<com.github.davityle.ngprocessor.xml.XmlNode>> getFileNodeMap(){
        List<File> layoutDirs = layoutsFinder.findLayouts();
        Map<File, List<com.github.davityle.ngprocessor.xml.XmlNode>> xmlAttrMap = new HashMap<>();

        for(File f : layoutDirs){
            for(File kid : f.listFiles()){
                if(kid.getName().endsWith(".xml")){
                    Option<Document> doc = getDocumentFromFile(kid);
                    if(doc.isAbsent()){
                        continue;
                    }

                    NodeList children = doc.get().getChildNodes();
                    String nameSpace = getNameSpace(children);
                    if(nameSpace != null){
                        String pattern = String.format(NAMESPACE_ATTRIBUTE_REG, nameSpace);
                        Pattern nameSpaceAttributePattern = Pattern.compile(pattern);
                        List<com.github.davityle.ngprocessor.xml.XmlNode> nodeList = new ArrayList<>();
                        getNgAttrNodes(doc.get(), nameSpaceAttributePattern, nodeList, kid.getName());
                        for(com.github.davityle.ngprocessor.xml.XmlNode xmlNode : nodeList){
                            for(XmlAttribute xmlAttribute : xmlNode.getAttributes()){
                                try {
                                    Source source = attributeCompiler.compile(xmlAttribute.getValue());
                                    xmlAttribute.setSource(source);
                                }catch(RuntimeException e){
                                    messageUtils.error(null,
                                            "Layout file '%s' has an invalid attribute '%s' in view '%s' with value '%s' because '%s'",
                                            kid,
                                            xmlAttribute.getName(),
                                            xmlNode.getId(),
                                            xmlAttribute.getValue(),
                                            e.getMessage()
                                    );
                                }
                            }
                        }
                        xmlAttrMap.put(kid, nodeList);
                    }
                }
            }
        }
        return xmlAttrMap;
    }

    private void getNgAttrNodes(Node n, Pattern attributePattern, List<com.github.davityle.ngprocessor.xml.XmlNode> ngAttrNodes, String fileName){
        if(n == null || !n.hasChildNodes())
            return;

        NodeList nodes = n.getChildNodes();
        for(int index = 0; index < nodes.getLength(); index++) {
            Node childNode = nodes.item(index);
            NamedNodeMap nodeMap = childNode.getAttributes();
            if(nodeMap != null){
                List<XmlAttribute> attributeList = null;
                String id = null;
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Node node = nodeMap.item(j);
                    Matcher idMatcher = ID_ATTR_PATTERN.matcher(node.toString());

                    if(idMatcher.matches()){
                        id = idMatcher.group(1);
                        continue;
                    }

                    Matcher matcher = attributePattern.matcher(node.toString());
                    if(matcher.matches()){
                        if(attributeList == null)
                            attributeList = new ArrayList<>();
                        String attr = matcher.group(1);
                        if(attributes.containsKey(attr)) {
                            attributeList.add(new XmlAttribute(attr, matcher.group(2)));
                        }
                    }

                }
                if(attributeList != null && !attributeList.isEmpty()){
                    if(id == null){
                        messageUtils.error(null, "xml attributes '%s' in node '%s' in layout file '%s' need an id", attributeList.toString(), childNode.toString(), fileName);
                    }else {
                        ngAttrNodes.add(new com.github.davityle.ngprocessor.xml.XmlNode(id, attributeList, fileName, childNode.getNodeName()));
                    }
                }
            }
            getNgAttrNodes(childNode, attributePattern, ngAttrNodes, fileName);
        }
    }

    private String getNameSpace(NodeList nodes){
        for(int index = 0; index < nodes.getLength(); index++) {
            NamedNodeMap nodeMap = nodes.item(index).getAttributes();
            if(nodeMap != null){
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Matcher matcher = NAME_SPACE_PATTERN.matcher(nodeMap.item(j).toString());
                    if(matcher.matches()){
                        return matcher.group(1);
                    }
                }
            }
        }
        for(int index = 0; index < nodes.getLength(); index++) {
            NodeList nodeList = nodes.item(index).getChildNodes();
            if(nodeList != null){
                String namespace = getNameSpace(nodeList);
                if(namespace != null)
                    return namespace;
            }
        }
        return null;
    }
}
