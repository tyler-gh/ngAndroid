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

package com.github.davityle.ngprocessor.util.xml;

import com.github.davityle.ngprocessor.attrcompiler.AttributeCompiler;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.util.LayoutsFinder;
import com.github.davityle.ngprocessor.util.MessageUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by tyler on 3/30/15.
 */
public class XmlUtils {

    private static final Pattern NAME_SPACE_PATTERN = Pattern.compile("xmlns:(.+)=\"http://schemas.android.com/apk/res-auto\"");
    private static final Pattern ID_ATTR_PATTERN = Pattern.compile("android:id=\"@\\+id/(.+)\"");
    private static final String NAMESPACE_ATTRIBUTE_REG = "%s:(.+)=\"(.+)\"";

    private static final Set<String> NG_ATTRS = Collections.unmodifiableSet(new HashSet<String>() {{
        add("ngModel");
        add("ngClick");
        add("ngLongClick");
        add("ngBlur");
        add("ngChange");
        add("ngDisabled");
        add("ngFocus");
        add("ngInvisible");
        add("ngGone");
        add("ngIf");
        add("ngRepeat");
        add("ngSrc");
        add("ngJsonSrc");
        add("ngText");
        add("ngOnItemClickListener");
    }});

    public static Document getDocumentFromFile(File file){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            MessageUtils.error(null, e.getMessage());
            return null;
        }
    }

    public static Map<File, List<XmlNode>> getFileNodeMap(){
        List<File> layoutDirs = LayoutsFinder.findLayouts();
        Map<File, List<XmlNode>> xmlAttrMap = new HashMap<>();

        for(File f : layoutDirs){
            for(File kid : f.listFiles()){
                if(kid.getName().endsWith(".xml")){
                    Document doc = getDocumentFromFile(kid);
                    if(doc == null){
                        continue;
                    }

                    NodeList children = doc.getChildNodes();
                    String nameSpace = getNameSpace(children);
                    if(nameSpace != null){
                        String pattern = String.format(NAMESPACE_ATTRIBUTE_REG, nameSpace);
                        Pattern nameSpaceAttributePattern = Pattern.compile(pattern);
                        List<XmlNode> nodeList = new ArrayList<>();
                        getNgAttrNodes(doc, nameSpaceAttributePattern, nodeList, kid.getName());
                        for(XmlNode xmlNode : nodeList){
                            for(XmlAttribute xmlAttribute : xmlNode.getAttributes()){
                                try {
                                    Source source = new AttributeCompiler(xmlAttribute.getValue()).compile();
                                    xmlAttribute.setSource(source);
                                }catch(RuntimeException e){
                                    MessageUtils.error(null,
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

    private static void getNgAttrNodes(Node n, Pattern attributePattern, List<XmlNode> ngAttrNodes, String fileName){
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
                        if(NG_ATTRS.contains(attr)) {
                            attributeList.add(new XmlAttribute(attr, matcher.group(2)));
                        }
                    }

                }
                if(attributeList != null){
                    if(id == null){
                        MessageUtils.error(null, "xml attributes '%s' in node '%s' in layout file '%s' need an id", attributeList.toString(), childNode.toString(), fileName);
                    }else {
                        ngAttrNodes.add(new XmlNode(id, attributeList, fileName, childNode.getNodeName()));
                    }
                }
            }
            getNgAttrNodes(childNode, attributePattern, ngAttrNodes, fileName);
        }
    }

    private static String getNameSpace(NodeList nodes){
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
