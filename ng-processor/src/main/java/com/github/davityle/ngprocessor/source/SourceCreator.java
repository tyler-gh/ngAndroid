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

package com.github.davityle.ngprocessor.source;

import com.github.davityle.ngprocessor.attributes.AttrDependency;
import com.github.davityle.ngprocessor.attributes.AttrPackageResolver;
import com.github.davityle.ngprocessor.attributes.Attribute;
import com.github.davityle.ngprocessor.source.links.NgModelSourceLink;
import com.github.davityle.ngprocessor.source.links.NgScopeSourceLink;
import com.github.davityle.ngprocessor.util.MessageUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
    private final Set<AttrDependency> attrDependencies;

    @Inject MessageUtils messageUtils;
    @Inject Filer filer;
    @Inject AttrPackageResolver attrPackageResolver;

    public SourceCreator(List<NgModelSourceLink> modelSourceLinks, List<NgScopeSourceLink> scopeSourceLinks, Set<AttrDependency> attrDependencies) {
        this.modelSourceLinks = modelSourceLinks;
        this.scopeSourceLinks = scopeSourceLinks;
        this.attrDependencies = attrDependencies;
    }

    public void createSourceFiles(){
        Properties props = new Properties();
        props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SystemLogChute");
        props.setProperty("resource.loader", "classpath");
        props.setProperty("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");


        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        Template vtModel = ve.getTemplate("templates/ngmodel.vm");
        Template vtScope = ve.getTemplate("templates/ngscope.vm");
        Template vtAttrs = ve.getTemplate("templates/attrs.vm");

        for(AttrDependency attrDependency : attrDependencies) {
            if(attrDependency.getSourceCode().isPresent()) {
                try {
                    JavaFileObject jfo = filer.createSourceFile(attrPackageResolver.getPackage() + "." + attrDependency.getClassName());
                    Writer writer = jfo.openWriter();
                    writer.write(attrDependency.getSourceCode().get());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    messageUtils.error(null, e.getMessage());
                }
            }
        }

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

        createAttributesClass(vtAttrs);
    }


    private void createAttributesClass(Template vtAttrs){
        Set<Attribute> attributes = new HashSet<>();
        for(AttrDependency attrDependency : attrDependencies) {
            if(attrDependency instanceof  Attribute){
                attributes.add((Attribute) attrDependency);
            }
        }

        VelocityContext context = new VelocityContext();

        context.put("attributes", attributes);

        try {
            JavaFileObject jfo = filer.createSourceFile(attrPackageResolver.getPackage() + "." + attrPackageResolver.getAttrClassName());
            Writer writer = jfo.openWriter();
            vtAttrs.merge(context, writer);
            writer.flush();
            writer.close();
        }catch (IOException e){
            messageUtils.error(null, e.getMessage());
        }


    }
}
