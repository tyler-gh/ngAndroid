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
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by tyler on 3/30/15.
 */
public class MessageUtils {

    private static ProcessingEnvironment processingEnv;

    public static void setProcessingEnv(ProcessingEnvironment processingEnv){
        MessageUtils.processingEnv = processingEnv;
    }

    public static void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
    }

    public static void warning(Element element, String message, Object... args) {
        printMessage(Kind.WARNING, element, message, args);
    }

    private static void printMessage(Kind kind, Element element, String message, Object... args){
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(kind, message, element);
    }
}
