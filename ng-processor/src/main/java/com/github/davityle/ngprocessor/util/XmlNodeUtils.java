package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.xml.XmlAttribute;
import com.github.davityle.ngprocessor.xml.XmlScope;
import com.github.davityle.ngprocessor.xml.XmlView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by tyler on 7/6/15.
 */
public class XmlNodeUtils {

    private final CollectionUtils collectionUtils;

    @Inject
    public XmlNodeUtils(CollectionUtils collectionUtils) {
        this.collectionUtils = collectionUtils;
    }

    public Set<String> getAttributes(Map<?, Collection<XmlScope>> nodeMap) {
        Set<String> attrs = new HashSet<>();

        for(Collection<XmlScope> nodes : nodeMap.values()) {
            for(XmlScope node : nodes) {
                attrs.addAll(collectionUtils.flatMap(node.getViews(), new CollectionUtils.Function<XmlView, Collection<String>>() {
                    @Override
                    public Collection<String> apply(XmlView view) {
                        return collectionUtils.map(view.getAttributes(), new CollectionUtils.Function<XmlAttribute, String>() {
                            @Override
                            public String apply(XmlAttribute xmlAttribute) {
                                return xmlAttribute.getName();
                            }
                        });
                    }
                }));
            }
        }

        return attrs;
    }

}
