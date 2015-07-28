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

import com.github.davityle.ngprocessor.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ScopeUtils {

    public static final String NG_SCOPE_ANNOTATION = "com.ngandroid.lib.annotations.NgScope";
    public static final String NG_MODEL_ANNOTATION = "com.ngandroid.lib.annotations.NgScope";
    public static final String SCOPE_APPENDAGE = "$$NgScope";

    private final ElementUtils elementUtils;
    private final RoundEnvironment roundEnv;
    private final MessageUtils messageUtils;
    private final CollectionUtils collectionUtils;

    @Inject
    public ScopeUtils(ElementUtils elementUtils, RoundEnvironment roundEnv, MessageUtils messageUtils, CollectionUtils collectionUtils){
        this.elementUtils = elementUtils;
        this.roundEnv = roundEnv;
        this.messageUtils = messageUtils;
        this.collectionUtils = collectionUtils;
    }

    public Set<Scope> getScopes(Set<? extends TypeElement> annotations){

        Collection<TypeElement> annotatedScopes = collectionUtils.filter(new ArrayList<>(annotations), new CollectionUtils.Function<TypeElement, Boolean>() {
            @Override
            public Boolean apply(TypeElement annotation) {
                return NG_SCOPE_ANNOTATION.equals(annotation.getQualifiedName().toString());
            }
        });

        final Collection<Element> ngModels = getModels(annotations);
        ngModels.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                if (!elementUtils.isAccessible(element)) {
                    messageUtils.error(Option.of(element), "Unable to access field '%s' from scope '%s'. Must have default or public access", element.toString(), element.getEnclosingElement().toString());
                }
            }
        });

        Collection<Scope> scopes = collectionUtils.flatMap(annotatedScopes, new CollectionUtils.Function<TypeElement, Collection<Scope>>() {
            @Override
            public Collection<Scope> apply(TypeElement annotation) {
                Collection<Element> javaScopes = new ArrayList<>(roundEnv.getElementsAnnotatedWith(annotation));
                return collectionUtils.map(javaScopes, new CollectionUtils.Function<Element, Scope>() {
                    @Override
                    public Scope apply(final Element scope) {
                        Set<Modifier> modifiers = scope.getModifiers();
                        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                            messageUtils.error(Option.of(scope), "Unable to access Scope '%s'. Must have default or public access", scope.toString());
                        }
                        return new Scope(scope, collectionUtils.filter(ngModels, new CollectionUtils.Function<Element, Boolean>() {
                            @Override
                            public Boolean apply(Element element) {
                                return element.getEnclosingElement().equals(scope);
                            }
                        }), getScopeName(scope));
                    }
                });
            }
        });

        return new HashSet<>(scopes);
    }

    public Collection<Element> getModels(Set<? extends TypeElement> annotations) {
        return collectionUtils.flatMap(collectionUtils.filter(new ArrayList<>(annotations), new CollectionUtils.Function<TypeElement, Boolean>() {
            @Override
            public Boolean apply(TypeElement annotation) {
                return NG_MODEL_ANNOTATION.equals(annotation.getQualifiedName().toString());
            }
        }), new CollectionUtils.Function<TypeElement, Collection<Element>>() {
            @Override
            public Collection<Element> apply(TypeElement annotation) {
                return new ArrayList<>(roundEnv.getElementsAnnotatedWith(annotation));
            }
        });
    }

    private String getScopeName(final Element scope) {
        return elementUtils.getAnnotationValue(scope, ScopeUtils.NG_SCOPE_ANNOTATION, "name", String.class).fold(new Option.OptionCB<String, String>() {
            @Override
            public String absent() {
                messageUtils.error(Option.of(scope), "Scope must have a name.");
                return "";
            }

            @Override
            public String present(String s) {
                System.out.println(s);
                return s;
            }
        });
    }
}
