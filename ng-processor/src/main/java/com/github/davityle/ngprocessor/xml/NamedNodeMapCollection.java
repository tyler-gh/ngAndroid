package com.github.davityle.ngprocessor.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Created by tyler on 7/22/15.
 */
public class NamedNodeMapCollection extends NodeCollection {


    public NamedNodeMapCollection(final NamedNodeMap nodeMap) {
        super(new Nodes() {
            @Override
            public Node item(int i) {
                System.out.println(i + "/" + getLength());
                return nodeMap.item(i);
            }

            @Override
            public int getLength() {
                return nodeMap.getLength();
            }
        });
    }
}
