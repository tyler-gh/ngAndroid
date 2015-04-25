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
 * Created by tyler on 2/2/15.
 */
public class TernarySource extends Source<TernarySource> {

    private Source booleanSource;
    private Source valTrue, valFalse;

    public TernarySource(Source booleanSource, Source valTrue, Source valFalse) {
        super(TypeUtils.getOperatorKind(valTrue, valFalse));
        this.booleanSource = booleanSource;
        this.valTrue = valTrue;
        this.valFalse = valFalse;
    }

    @Override
    public String getSource() {
        return booleanSource.getSource() + '?' + valTrue.getSource() + ':' + valFalse.getSource();
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        booleanSource.getModelSource(models);
        valFalse.getModelSource(models);
        valTrue.getModelSource(models);
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {
        booleanSource.getMethodSource(methods);
        valFalse.getMethodSource(methods);
        valTrue.getMethodSource(methods);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    protected TernarySource cp(TypeMirror typeMirror) throws IllegalArgumentException {
        return new TernarySource(booleanSource, valTrue, valFalse);
    }

    public Source getBooleanSource() {
        return booleanSource;
    }

    public void setBooleanSource(Source booleanSource) {
        this.booleanSource = booleanSource;
    }

    public Source getValTrue() {
        return valTrue;
    }

    public void setValTrue(Source valTrue) {
        this.valTrue = valTrue;
    }

    public Source getValFalse() {
        return valFalse;
    }

    public void setValFalse(Source valFalse) {
        this.valFalse = valFalse;
    }
}
