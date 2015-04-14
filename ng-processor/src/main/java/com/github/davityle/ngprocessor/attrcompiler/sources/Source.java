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
 * Created by davityle on 1/24/15.
 */
public abstract class Source <T extends Source> {

    private TypeMirror typeMirror;

    protected Source(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public abstract String getSource();
    public abstract void getModelSource(List<ModelSource> models);
    public abstract void getMethodSource(List<MethodSource> methods);
    protected abstract T cp(TypeMirror typeMirror) throws IllegalArgumentException;

    public T copy(TypeMirror typeMirror) throws IllegalArgumentException{
        TypeMirror current = getTypeMirror();
        if(current != null && !TypeUtils.match(current, typeMirror))
            throw new IllegalArgumentException(current + " cannot be assigned to " + typeMirror);
        return cp(typeMirror);
    }

    public T copy() throws IllegalArgumentException{
        return copy(getTypeMirror());
    }
    public String toString(){
        return getSource();
    }

    public void setTypeMirror(TypeMirror typeMirror){
        this.typeMirror = typeMirror;
    }

    public TypeMirror getTypeMirror(){
//        if(typeMirror == null)
//            throw new IllegalStateException("TypeMirror is null for source '" + this + "'");
        return typeMirror;
    }
}
