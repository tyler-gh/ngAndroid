package com.github.davityle.ngprocessor.finders;

import com.github.davityle.ngprocessor.util.Option;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class NamespaceFinder {

    private static final Pattern NAME_SPACE_PATTERN = Pattern.compile("xmlns:(.+)=\"http://schemas.android.com/apk/res-auto\"");

    @Inject
    public NamespaceFinder(){}

    public Option<String> getNameSpace(Document doc){
        return getNameSpace(doc.getChildNodes());
    }

    public Option<String> getNameSpace(NodeList nodes){
        for(int index = 0; index < nodes.getLength(); index++) {
            NamedNodeMap nodeMap = nodes.item(index).getAttributes();
            if(nodeMap != null){
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Matcher matcher = NAME_SPACE_PATTERN.matcher(nodeMap.item(j).toString());
                    if(matcher.matches()){
                        return Option.of(matcher.group(1));
                    }
                }
            }
        }
        for(int index = 0; index < nodes.getLength(); index++) {
            NodeList nodeList = nodes.item(index).getChildNodes();
            if(nodeList != null){
                Option<String> namespace = getNameSpace(nodeList);
                if(namespace.isPresent())
                    return namespace;
            }
        }
        return Option.absent();
    }
}
