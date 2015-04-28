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

import com.github.davityle.ngprocessor.manifestfinders.AndroidManifest;
import com.github.davityle.ngprocessor.manifestfinders.AndroidManifestFinder;
import com.github.davityle.ngprocessor.manifestfinders.Option;
import com.github.davityle.ngprocessor.util.ManifestFinder;
import com.github.davityle.ngprocessor.util.MessageUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by tyler on 4/7/15.
 */
public class ManifestPackageUtils {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile(".*package=\"(.*)\"");

    public static String getPackageName(ProcessingEnvironment processingEnvironment){

        AndroidManifestFinder finder = new AndroidManifestFinder(processingEnvironment);
        Option<AndroidManifest> option = finder.extractAndroidManifest();
        if(option.isPresent()) {
            String packageName = option.get().getApplicationPackage();
            if(packageName != null)
                return packageName;
        }

        File manifest = ManifestFinder.findManifest();

        if(manifest == null) {
            MessageUtils.error(null, "Unable to find android manifest.");
            return null;
        }

        Document doc = XmlUtils.getDocumentFromFile(manifest);
        if(doc == null)
            return null;

        return getPackagePattern(doc.getChildNodes());
    }

    private static String getPackagePattern(NodeList nodes){
        for(int index = 0; index < nodes.getLength(); index++) {
            NamedNodeMap nodeMap = nodes.item(index).getAttributes();
            if(nodeMap != null){
                for(int j = 0; j < nodeMap.getLength(); j++){
                    Matcher matcher = PACKAGE_PATTERN.matcher(nodeMap.item(j).toString());
                    if(matcher.matches()){
                        return matcher.group(1);
                    }
                }
            }
        }
        for(int index = 0; index < nodes.getLength(); index++) {
            NodeList nodeList = nodes.item(index).getChildNodes();
            if(nodeList != null){
                String namespace = getPackagePattern(nodeList);
                if(namespace != null)
                    return namespace;
            }
        }
        return null;
    }
}
