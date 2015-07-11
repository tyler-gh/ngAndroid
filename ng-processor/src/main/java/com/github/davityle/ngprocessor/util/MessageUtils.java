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

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;

import javax.tools.Diagnostic.Kind;

/**
 * Created by tyler on 3/30/15.
 */
public class MessageUtils {

    private final ProcessingEnvironment processingEnv;
    private boolean hasErrors = false;
    
    @Inject
    public MessageUtils(ProcessingEnvironment processingEnv){
        this.processingEnv = processingEnv;
    }

    public void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
        hasErrors = true;
    }

    public void note(Element element, String message, Object... args){
        printMessage(Kind.NOTE, element, message, args);
    }

    public void warning(Element element, String message, Object... args) {
        printMessage(Kind.WARNING, element, message, args);
    }

    private void printMessage(Kind kind, Element element, String message, Object... args){
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(kind, message, element);
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
