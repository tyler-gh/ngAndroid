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

package com.github.davityle.ngprocessor.attrcompiler.sources;


import com.github.davityle.ngprocessor.util.TypeUtils;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by tyler on 2/10/15.
 */
public class KnotSource extends Source<KnotSource> {

    private Source source;

    public KnotSource(TypeUtils typeUtils, Source source) {
        super(typeUtils, typeUtils.getBooleanType());
        this.source = source;
    }

    @Override
    public String getSource() {
        return '!' + source.getSource();
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        source.getModelSource(models);
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {
        source.getMethodSource(methods);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    protected KnotSource cp(TypeMirror typeMirror) throws IllegalArgumentException {
        return new KnotSource(typeUtils, source.copy());
    }

    public Source getBooleanSource(){
        return source;
    }

    public void setBooleanSource(Source booleanSource) {
        this.source = booleanSource;
    }
}
