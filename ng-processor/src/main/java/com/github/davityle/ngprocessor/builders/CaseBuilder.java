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

package com.github.davityle.ngprocessor.builders;

/**
 * Created by tyler on 3/24/15.
 */
public class CaseBuilder {

    private final StringBuilder caseBuilder;
    private final String indent;

    public CaseBuilder(String value, String indent){
        this.indent = indent;
        caseBuilder = new StringBuilder(indent);
        caseBuilder.append("case \"")
                .append(value)
                .append("\":\n");
    }

    public CaseBuilder addLine(String line){
        caseBuilder.append(indent)
                .append('\t')
                .append(line)
                .append('\n');
        return this;
    }

    public CaseBuilder returnVal(String val){
        caseBuilder.append(indent)
                .append("\t return ")
                .append(val)
                .append('\n');
        return this;
    }

    public CaseBuilder returnVoid(){
        caseBuilder.append(indent)
                .append("\treturn;\n");
        return this;
    }

    public CaseBuilder breakCase(){
        caseBuilder.append(indent)
                .append("\tbreak;\n");
        return this;
    }

    @Override
    public String toString() {
        return caseBuilder.toString();
    }
}
