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

package com.github.davityle.ngprocessor.finders;

import com.github.davityle.ngprocessor.util.Option;

import java.io.File;

import javax.inject.Inject;

public class ManifestFinder {

    @Inject
    public ManifestFinder(){

    }

    public Option<File> findManifest(){
        return findManifest(new File("."));
    }

    private Option<File> findManifest(File dir){
        File[] kids = dir.listFiles();
        if(kids != null) {
            for (File file : kids) {
                String name = file.getName();
                if (file.isDirectory()) {
                    Option<File> f = findManifest(file);
                    if(f.isPresent())
                        return f;
                }else{
                    if(name.equals("AndroidManifest.xml")){
                        return Option.of(file);
                    }
                }
            }
        }
        return Option.absent();
    }
}
