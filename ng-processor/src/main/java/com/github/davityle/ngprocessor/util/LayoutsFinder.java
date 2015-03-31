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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 3/30/15.
 */
public class LayoutsFinder {

    public static  List<File> findLayouts(){
        File root = new File(".");
        List<File> files = new ArrayList<>();
        findLayouts(root, files);
        return files;
    }

    private static void findLayouts(File f, List<File> files){
        File[] kids = f.listFiles();
        if(kids != null) {
            for (File file : kids) {
                String name = file.getName();
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
