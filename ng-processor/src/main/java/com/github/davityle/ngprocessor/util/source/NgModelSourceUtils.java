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

import com.github.davityle.ngprocessor.SourceField;
import com.github.davityle.ngprocessor.sourcelinks.NgModelSourceLink;
import com.github.davityle.ngprocessor.util.ElementUtils;
import com.github.davityle.ngprocessor.util.MessageUtils;
import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 3/30/15.
 */
public class NgModelSourceUtils {

    public static List<NgModelSourceLink> getSourceLinks(Map<String, Element> modelBuilderMap){
        Set<Map.Entry<String, Element>> models = modelBuilderMap.entrySet();
        List<NgModelSourceLink> modelSourceLinks = new ArrayList<>();
        for(Map.Entry<String, Element> model : models){
            Element element = model.getValue();
            modelSourceLinks.add(NgModelSourceUtils.getSourceLink(element));
        }
        return modelSourceLinks;
    }

    private static NgModelSourceLink getSourceLink(Element element) {

        TypeMirror fieldType = element.asType();
        String fieldTypeName = fieldType.toString();
        int periodindex = fieldTypeName.lastIndexOf('.');
        TypeElement typeElement = TypeUtils.asTypeElement(fieldType);
        String packageName = ElementUtils.getPackageName(typeElement);

        List<SourceField> fields = new ArrayList<>();

        String modelName = fieldTypeName.substring(periodindex + 1);

        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        boolean isInterface = typeElement.getKind().isInterface();

        for(int index = 0; index < enclosedElements.size(); index++){
            Element enclosedElement = enclosedElements.get(index);
            if(ElementUtils.isSetter(enclosedElement)) {
                ExecutableElement setter = (ExecutableElement) enclosedElement;
                if (!ElementUtils.returnsVoid(setter)) {
                    MessageUtils.error(element, "Setter '%s' must not return a value", element.toString());
                    continue;
                }
                Set<Modifier> modifiers = setter.getModifiers();
                if(modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)){
                    MessageUtils.error(element, "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
                    continue;
                }
                String fName = setter.getSimpleName().toString().substring(3).toLowerCase();
                TypeMirror typeMirror = setter.getParameters().get(0).asType();
                String type = typeMirror.toString();
                SourceField sourceField = new SourceField(fName, type);
                sourceField.setSetter(setter.getSimpleName().toString());
                // TODO O(n^2) is the best
                boolean getterFound = false;
                for(Element possGetter : enclosedElements) {
                    if(ElementUtils.isGetterForField(possGetter, fName, typeMirror.getKind())){
                        sourceField.setGetter(possGetter.getSimpleName().toString());
                        getterFound = true;
                        break;
                    }
                }
                if(!getterFound){
                    MessageUtils.error(enclosedElement, "Field '%s' is missing a corresponding getter", fName);
                }
                fields.add(sourceField);
            }
        }
        return new NgModelSourceLink(modelName, packageName, isInterface, fields, element);
    }
}
