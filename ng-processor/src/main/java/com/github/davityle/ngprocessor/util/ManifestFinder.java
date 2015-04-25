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

/**
 * Created by tyler on 4/7/15.
 */
public class ManifestFinder {

    public static File findManifest(){
        return findManifest(new File("."));
    }

    private static File findManifest(File dir){
        File[] kids = dir.listFiles();
        if(kids != null) {
            for (File file : kids) {
                String name = file.getName();
                if (file.isDirectory()) {
                    File f = findManifest(file);
                    if(f != null)
                        return f;
                }else{
                    if(name.equals("AndroidManifest.xml")){
                        return file;
                    }
                }
            }
        }
        return null;
    }
}
