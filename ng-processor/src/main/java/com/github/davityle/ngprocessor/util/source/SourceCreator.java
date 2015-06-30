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

package com.github.davityle.ngprocessor.util.source;

import com.github.davityle.ngprocessor.sourcelinks.NgModelSourceLink;
import com.github.davityle.ngprocessor.sourcelinks.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.MessageUtils;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

/**
 * Created by tyler on 3/30/15.
 */
public class SourceCreator {

    private final List<NgModelSourceLink> modelSourceLinks;
    private final List<NgScopeSourceLink> scopeSourceLinks;

    @Inject MessageUtils messageUtils;
    @Inject Filer filer;

    public SourceCreator(List<NgModelSourceLink> modelSourceLinks, List<NgScopeSourceLink> scopeSourceLinks) {
        this.modelSourceLinks = modelSourceLinks;
        this.scopeSourceLinks = scopeSourceLinks;
    }

    public void createSourceFiles(){
        Properties props = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        Template vtModel = ve.getTemplate("templates/ngmodel.vm");
        Template vtScope = ve.getTemplate("templates/ngscope.vm");

        for (NgModelSourceLink ms : modelSourceLinks){
            try {
                JavaFileObject jfo = filer.createSourceFile(ms.getSourceFileName(), ms.getElements());
                Writer writer = jfo.openWriter();
                vtModel.merge(ms.getVelocityContext(), writer);
                writer.flush();
                writer.close();
            }catch (IOException e){
                messageUtils.error(ms.getElements()[0], e.getMessage());
            }
        }

        for(NgScopeSourceLink ns : scopeSourceLinks){
            try {
                JavaFileObject jfo = filer.createSourceFile(ns.getSourceFileName(), ns.getElements());
                Writer writer = jfo.openWriter();
                vtScope.merge(ns.getVelocityContext(), writer);
                writer.flush();
                writer.close();
            }catch (IOException e){
                Element[] elements = ns.getElements();
                messageUtils.error(elements[elements.length - 1], e.getMessage());
            }
        }
    }
}
