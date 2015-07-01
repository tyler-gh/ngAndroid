package com.github.davityle.ngprocessor.xml;

import java.util.List;

/**
 * Created by tyler on 4/22/15.
 */
public class TypedXmlNode extends XmlNode {

    public TypedXmlNode(XmlNode xmlNode, List<TypedXmlAttribute> typedAttributes) {
        super(xmlNode.getId(), typedAttributes, xmlNode.getLayoutName(), xmlNode.getElementType());
    }
}
