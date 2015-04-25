package com.github.davityle.ngprocessor.util.xml;

import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;

import java.util.List;
import java.util.Map;

/**
 * Created by tyler on 4/22/15.
 */
public class TypedXmlNode extends XmlNode {

    public TypedXmlNode(XmlNode xmlNode, List<TypedXmlAttribute> typedAttributes) {
        super(xmlNode.getId(), typedAttributes, xmlNode.getLayoutName(), xmlNode.getElementType());
    }
}
