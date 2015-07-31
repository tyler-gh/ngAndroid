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

import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.lang.model.element.Element;

/**
 * Created by tyler on 3/30/15.
 */
public class LayoutsFinder {

    private final MessageUtils messageUtils;
    private final DefaultLayoutDirProvider defaultLayoutDirProvider;

    @Inject
    public LayoutsFinder(MessageUtils messageUtils, DefaultLayoutDirProvider defaultLayoutDirProvider){
        this.messageUtils = messageUtils;
        this.defaultLayoutDirProvider = defaultLayoutDirProvider;
    }

    public List<File> findLayoutDirs(){
        return defaultLayoutDirProvider.getDefaultLayoutDir().fold(new Option.OptionCB<String, List<File>>() {
            @Override
            public List<File> absent() {
                String path = System.getProperty("LAYOUT_PATH", null);

                if(path != null){
                    return getFileFromPath(path);
                }

                return findLayoutDirs(new File("."));
            }

            @Override
            public List<File> present(String s) {
                return getFileFromPath(s);
            }
        });
    }

    private List<File> getFileFromPath(String path){
        File file = new File(path);
        if(!file.exists()){
            messageUtils.error(Option.<Element>absent(), "The layout file path '%s' does not exist", path);
            return new ArrayList<>();
        }
        return Collections.singletonList(file);
    }

    private List<File> findLayoutDirs(File f){
        File[] kids = f.listFiles();
        if(kids != null) {
            for (File file : kids) {
                if (isNonBuildDirectory(file)) {
                    File resourceFile = new File(file, "src/main/res/");
                    if (resourceFile.exists() && resourceFile.isDirectory()) {
                        List<File> dirs = new ArrayList<>();
                        findLayoutDirs(file, dirs);
                        return dirs;
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private void findLayoutDirs(File f, List<File> files){
        File[] kids = f.listFiles();
        if(kids != null) {
            for (File file : kids) {
                if (isNonBuildDirectory(file)) {
                    if (file.getName().equals("layout")) {
                        files.add(file);
                    } else {
                        findLayoutDirs(file, files);
                    }
                }
            }
        }
    }

    private boolean isNonBuildDirectory(File file) {
        String name = file.getName();
        return file.isDirectory() && !name.equals("compile") && !name.equals("bin") && !name.equals("build");
    }
}
