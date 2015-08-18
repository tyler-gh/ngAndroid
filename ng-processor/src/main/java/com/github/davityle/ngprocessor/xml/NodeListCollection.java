package com.github.davityle.ngprocessor.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListCollection extends NodeCollection {

    public NodeListCollection(final NodeList nodeList) {
        super(new Nodes() {
            @Override
            public Node item(int i) {
                return nodeList.item(i);
            }

            @Override
            public int getLength() {
                return nodeList.getLength();
            }
        });
    }
}