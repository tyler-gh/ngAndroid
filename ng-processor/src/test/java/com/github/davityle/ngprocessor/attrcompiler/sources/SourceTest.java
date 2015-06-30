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

import com.github.davityle.ngprocessor.attrcompiler.AttributeCompiler;
import com.github.davityle.ngprocessor.util.TypeUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SourceTest {

    @Test
    public void testGetModelSource() throws Exception {
        AttributeCompiler builder = new AttributeCompiler(mock(TypeUtils.class));
        Source source = builder.compile("(modelName.num - (2*(7-1))) - 10/(modelName.num-3)");
        List<ModelSource> modelSourceList = new ArrayList<>();
        source.getModelSource(modelSourceList);
        assertEquals(2, modelSourceList.size());
        for(ModelSource modelSource : modelSourceList){
            assertEquals("modelName", modelSource.getModelName());
            assertEquals("num", modelSource.getFieldName());
        }
    }

    @Test
    public void testGetMethodSource() throws Exception {

    }
}