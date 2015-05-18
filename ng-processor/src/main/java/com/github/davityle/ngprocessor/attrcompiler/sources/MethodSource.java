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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
* Created by davityle on 1/24/15.
*/
public class MethodSource extends Source<MethodSource> {

    private final String methodName;
    private List<Source> parameters;
    private String parametersSource;

    public MethodSource(String source, List<Source> parameters) {
        this(source, parameters, null);
    }

    public MethodSource(String source, List<Source> parameters, TypeMirror typeMirror) {
        super(typeMirror);
        this.methodName = source;
        this.parameters = parameters;
    }

    @Override
    public String getSource() {
        if(parametersSource == null){
            StringBuilder parametersSourceBuilder = new StringBuilder("(");
            for(int index = 0; index < parameters.size(); index++){
                Source source = parameters.get(index);
                parametersSourceBuilder.append(source.getSource());
                if(index != parameters.size() -1)
                    parametersSourceBuilder.append(",");
            }
            parametersSourceBuilder.append(")");
            parametersSource = parametersSourceBuilder.toString();
        }
        // TODO put constant 'scope.' somewhere
        return "scope." + methodName + parametersSource;
    }

    public String getMethodName(){
        return methodName;
    }

    @Override
    public void getModelSource(List<ModelSource> models) {
        for(Source source : parameters){
            source.getModelSource(models);
        }
    }

    @Override
    public void getMethodSource(List<MethodSource> methods) {
        methods.add(this);
        for(Source source : parameters){
            source.getMethodSource(methods);
        }
    }

    @Override
    public boolean isVoid() {
        return getTypeMirror() == null || getTypeMirror().getKind() == TypeKind.VOID;
    }

    public void setParameters(List<Source> parameters) {
        this.parameters = parameters;
        parametersSource = null;
    }

    public List<Source> getParameters(){
        return Collections.unmodifiableList(parameters);
    }

    @Override
    protected MethodSource cp(TypeMirror typeMirror) {
        ArrayList<Source> paramCopy = new ArrayList<Source>();
        for(Source source : parameters){
            paramCopy.add(source.copy());
        }
        return new MethodSource(methodName, paramCopy, typeMirror);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof MethodSource))
            return false;
        MethodSource ms = (MethodSource) obj;
        return getMethodName().equals(ms.getMethodName()) && getSource().equals(ms.getSource());
    }

    @Override
    public int hashCode() {
        return getMethodName().hashCode() * 17 + getSource().hashCode() * 7;
    }
}
