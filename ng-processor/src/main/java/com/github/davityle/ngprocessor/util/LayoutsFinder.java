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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tyler on 3/30/15.
 */
public class LayoutsFinder {

    public static  List<File> findLayouts(String dir){

        if(dir != null)
            return getFileFromPath(dir);

        String path = System.getProperty("LAYOUT_PATH", null);

        if(path != null){
            return getFileFromPath(path);
        }

        URL url = Thread.currentThread().getContextClassLoader().getResource("layout/");
        if(url != null) {
            try {
                File layoutDir = new File(url.toURI());
                if(layoutDir.exists() && layoutDir.isDirectory())
                    return Collections.singletonList(layoutDir);
            } catch (URISyntaxException e) {
                MessageUtils.note(null, e.getMessage());
            }
        }

        File root = new File(".");
        List<File> files = new ArrayList<File>();
        findLayouts(root, files);
        return files;
    }

    private static List<File> getFileFromPath(String path){
        File file = new File(path);
        if(!file.exists()){
            MessageUtils.error(null, "The layout file path '%s' does not exist", path);
            return new ArrayList<File>();
        }
        return Collections.singletonList(file);
    }

    private static void findLayouts(File f, List<File> files){
        File[] kids = f.listFiles();
        if(kids != null) {
            for (File file : kids) {
                String name = file.getName();
                System.out.println(name);
                if (file.isDirectory() && !name.equals("compile") && !name.equals("bin")) {
                    if (file.getName().equals("layout")) {
                        files.add(file);
                    } else {
                        findLayouts(file, files);
                    }
                }
            }
        }
    }
}
