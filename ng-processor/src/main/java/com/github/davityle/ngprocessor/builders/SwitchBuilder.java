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
public class SwitchBuilder {

    private final StringBuilder switchBuilder;
    private final String indent;
    private boolean finished;

    public SwitchBuilder(String of, String indent) {
        this.indent = indent;
        this.switchBuilder = new StringBuilder(indent);
        switchBuilder.append("switch(")
                .append(of)
                .append("){\n");
        finished = false;
    }

    public CaseBuilder getCaseBuilder(String value){
        return new CaseBuilder(value, indent + '\t');
    }

    public SwitchBuilder addCase(CaseBuilder casebuilder){
        if(finished)
            throw new IllegalStateException("Switch has finished");
        switchBuilder.append(casebuilder);
        return this;
    }

    public SwitchBuilder finish(String error){
        switchBuilder.append(indent)
                .append("}\n");
        finished = true;
        return this;
    }

    public boolean isFinished(){
        return finished;
    }

    @Override
    public String toString() {
        return switchBuilder.toString();
    }
}
