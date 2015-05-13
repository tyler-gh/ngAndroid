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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        AttributeCompiler builder = new AttributeCompiler("(3 + (2)) - 10/5");
        Source source = builder.compile();
        assertEquals("((3+2)-(10/5))", source.getSource());
        assertEquals((3 + (2)) - 10/5, ((3+2)-(10/5)));


        builder = new AttributeCompiler("(3 - (2)) - 10/5");
        source = builder.compile();
        assertEquals("((3-2)-(10/5))", source.getSource());
        assertEquals((3 - (2)) - 10/5, ((3-2)-(10/5)));

        builder = new AttributeCompiler("(3 - (2+7)) - 10/5");
        source = builder.compile();
        assertEquals("((3-(2+7))-(10/5))", source.getSource());
        assertEquals((3 - (2+7)) - 10/5, ((3-(2+7))-(10/5)));
        assertEquals(-8, ((3-(2+7))-(10/5)));

        builder = new AttributeCompiler("(3 - (2+7)) - 10/(5-3)");
        source = builder.compile();
        assertEquals("((3-(2+7))-(10/(5-3)))", source.getSource());
        assertEquals((3 - (2+7)) - 10/(5-3), ((3-(2+7))-(10/(5-3))));

        builder = new AttributeCompiler("(modelName.num - (2*(7-1))) - 10/(modelName.num-3)");
        source = builder.compile();
        List<ModelSource> modelSourceList = new ArrayList<>();
        source.getModelSource(modelSourceList);
        for(ModelSource modelSource : modelSourceList)
            modelSource.setMethod("getNum");
        assertEquals("((modelName.getNum()-(2*(7-1)))-(10/(modelName.getNum()-3)))", source.getSource());
    }

    @Test
    public void testStringCompilation(){
        AttributeCompiler builder = new AttributeCompiler("xyz('Martin Luther King Jr.')");
        Source getter = builder.compile();
        assertEquals("scope.xyz(\"Martin Luther King Jr.\")", getter.getSource());

        builder = new AttributeCompiler("xyz('Martin Luther King\\'s Jr.')");
        getter = builder.compile();
        assertEquals("scope.xyz(\"Martin Luther King's Jr.\")", getter.getSource());


        builder = new AttributeCompiler("xyz('Martin \"Luther\" King\\'s Jr.')");
        getter = builder.compile();
        assertEquals("scope.xyz(\"Martin \\\"Luther\\\" King's Jr.\")", getter.getSource());

    }

    @Test
    public void testStringAdditionCompilation(){
        AttributeCompiler builder = new AttributeCompiler("getStringValue() + 'orange ' + getIntValue()");
        Source getter = builder.compile();
        assertEquals("((scope.getStringValue()+\"orange \")+scope.getIntValue())", getter.getSource());

        builder = new AttributeCompiler("isTrue() ? 'abcdefg ' + getIntValue() : getStringValue()");
        getter = builder.compile();
        assertEquals("scope.isTrue()?(\"abcdefg \"+scope.getIntValue()):scope.getStringValue()", getter.getSource());
    }

}