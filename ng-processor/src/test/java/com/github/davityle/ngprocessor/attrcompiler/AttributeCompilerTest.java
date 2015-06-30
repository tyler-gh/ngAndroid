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

package com.github.davityle.ngprocessor.attrcompiler;

import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.util.TypeUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AttributeCompilerTest {


    @Test
    public void testBug(){
//        try{
//            new AttributeCompiler("multiply(input.integer,2)").compile();
//        }catch(Exception e){
//            fail(e.getMessage());
//        }
    }

    @Test
    public void testNestedExpressions(){
        AttributeCompiler builder = new AttributeCompiler(mock(TypeUtils.class));

        Source source = builder.compile("(3 + (2)) - 10/5");
        assertEquals("((3+2)-(10/5))", source.getSource());
        assertEquals((3 + (2)) - 10/5, ((3+2)-(10/5)));


        source = builder.compile("(3 - (2)) - 10/5");
        assertEquals("((3-2)-(10/5))", source.getSource());
        assertEquals((3 - (2)) - 10/5, ((3-2)-(10/5)));

        source = builder.compile("(3 - (2+7)) - 10/5");
        assertEquals("((3-(2+7))-(10/5))", source.getSource());
        assertEquals((3 - (2+7)) - 10/5, ((3-(2+7))-(10/5)));
        assertEquals(-8, ((3-(2+7))-(10/5)));

        source = builder.compile("(3 - (2+7)) - 10/(5-3)");
        assertEquals("((3-(2+7))-(10/(5-3)))", source.getSource());
        assertEquals((3 - (2+7)) - 10/(5-3), ((3-(2+7))-(10/(5-3))));

        source = builder.compile("(modelName.num - (2*(7-1))) - 10/(modelName.num-3)");
        List<ModelSource> modelSourceList = new ArrayList<>();
        source.getModelSource(modelSourceList);
        for(ModelSource modelSource : modelSourceList)
            modelSource.setGetter("getNum");
        assertEquals("((scope.modelName.getNum()-(2*(7-1)))-(10/(scope.modelName.getNum()-3)))", source.getSource());
    }

    @Test
    public void testStringCompilation(){
        AttributeCompiler builder = new AttributeCompiler(mock(TypeUtils.class));

        Source getter = builder.compile("xyz('Martin Luther King Jr.')");
        assertEquals("scope.xyz(\"Martin Luther King Jr.\")", getter.getSource());

        getter = builder.compile("xyz('Martin Luther King\\'s Jr.')");
        assertEquals("scope.xyz(\"Martin Luther King's Jr.\")", getter.getSource());

        getter = builder.compile("xyz('Martin \"Luther\" King\\'s Jr.')");
        assertEquals("scope.xyz(\"Martin \\\"Luther\\\" King's Jr.\")", getter.getSource());

    }

    @Test
    public void testStringAdditionCompilation(){
        AttributeCompiler builder = new AttributeCompiler(mock(TypeUtils.class));

        Source getter = builder.compile("getStringValue() + 'orange ' + getIntValue()");
        assertEquals("((scope.getStringValue()+\"orange \")+scope.getIntValue())", getter.getSource());

        getter = builder.compile("isTrue() ? 'abcdefg ' + getIntValue() : getStringValue()");
        assertEquals("scope.isTrue()?(\"abcdefg \"+scope.getIntValue()):scope.getStringValue()", getter.getSource());
    }

}