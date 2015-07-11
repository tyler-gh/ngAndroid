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

package com.github.davityle.ngprocessor.util;

import com.github.davityle.ngprocessor.finders.AndroidManifestFinder;
import com.github.davityle.ngprocessor.finders.ManifestFinder;
import com.github.davityle.ngprocessor.xml.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by tyler on 4/7/15.
 */
public class ManifestPackageUtils {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile(".*package=\"(.*)\"");

    private final MessageUtils messageUtils;
    private final XmlUtils xmlUtils;
    private final AndroidManifestFinder finder;
    private final ManifestFinder manifestFinder;

    @Inject
    public ManifestPackageUtils(MessageUtils messageUtils, XmlUtils xmlUtils, AndroidManifestFinder finder, ManifestFinder manifestFinder){
        this.messageUtils = messageUtils;
        this.xmlUtils = xmlUtils;
        this.finder = finder;
        this.manifestFinder = manifestFinder;
    }

    public Option<String> getPackageName(){
        return finder.extractAndroidManifest().fold(new Option.OptionCB<String, Option<String>>() {
            @Override
            public Option<String> absent() {
                return findManifest();
            }

            @Override
            public Option<String> present(String s) {
                return s != null ? Option.of(s) : findManifest();
            }
        });
    }

    private Option<String> findManifest(){
        return manifestFinder.findManifest().fold(new Option.OptionCB<File, Option<String>>() {
            @Override
            public Option<String> absent() {
                messageUtils.error(null, "Unable to find android manifest.");
                return Option.absent();
            }

            @Override
            public Option<String> present(File file) {
                Option<Document> doc = xmlUtils.getDocumentFromFile(file);
                if(doc.isAbsent())
                    return Option.absent();

                return getPackagePattern(doc.get().getChildNodes());
            }
        });
    }

    private static Option<String> getPackagePattern(NodeList nodes){
        for(int index = 0; index < nodes.getLength(); index++) {
            NamedNodeMap nodeMap = nodes.item(index).getAttributes();
            if(nodeMap != null){
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Matcher matcher = PACKAGE_PATTERN.matcher(nodeMap.item(j).toString());
                    if(matcher.matches()){
                        String packageName = matcher.group(1);
                        if(!packageName.startsWith("android.support"))
                            return Option.of(packageName);
                    }
                }
            }
        }
        for(int index = 0; index < nodes.getLength(); index++) {
            NodeList nodeList = nodes.item(index).getChildNodes();
            if(nodeList != null){
                Option<String> namespace = getPackagePattern(nodeList);
                if(namespace.isPresent())
                    return namespace;
            }
        }
        return Option.absent();
    }
}
