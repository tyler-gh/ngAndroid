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

import javax.inject.Inject;

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

    public List<File> findLayouts(){
        return defaultLayoutDirProvider.getDefaultLayoutDir().fold(new Option.OptionCB<String, List<File>>() {
            @Override
            public List<File> absent() {
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
                        messageUtils.note(null, e.getMessage());
                    }
                }

                File root = new File(".");
                List<File> files = new ArrayList<>();
                findLayouts(root, files);
                return files;
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
            messageUtils.error(null, "The layout file path '%s' does not exist", path);
            return new ArrayList<>();
        }
        return Collections.singletonList(file);
    }

    private void findLayouts(File f, List<File> files){
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
