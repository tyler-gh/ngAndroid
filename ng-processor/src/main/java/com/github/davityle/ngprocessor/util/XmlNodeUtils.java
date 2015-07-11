package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.xml.XmlNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by tyler on 7/6/15.
 */
public class XmlNodeUtils {
    @Inject
    public XmlNodeUtils() {

    }

    public Set<String> getAttributes(Map<? extends Object, List<XmlNode>> nodeMap) {
        Set<String> attrs = new HashSet<>();

        for(List<XmlNode> nodes : nodeMap.values()) {
            for(XmlNode node : nodes) {
                attrs.addAll(node.getAttrs());
            }
        }

        return attrs;
    }

}
