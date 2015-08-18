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

import com.github.davityle.ngprocessor.util.Option;

import org.junit.Test;

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
        Option<String> source = AttributeCompiler.translateToJavaGetter("(3 + (2)) - 10/5");
        assertEquals("((3+2)-(10/5))", source.get());
        assertEquals((3 + (2)) - 10/5, ((3+2)-(10/5)));

        source = AttributeCompiler.translateToJavaGetter("(3 - (2)) - 10/5");
        assertEquals("((3-2)-(10/5))", source.get());
        assertEquals((3 - (2)) - 10/5, ((3-2)-(10/5)));

        source = AttributeCompiler.translateToJavaGetter("(3 - (2+7)) - 10/5");
        assertEquals("((3-(2+7))-(10/5))", source.get());
        assertEquals((3 - (2+7)) - 10/5, ((3-(2+7))-(10/5)));
        assertEquals(-8, ((3-(2+7))-(10/5)));

        source = AttributeCompiler.translateToJavaGetter("(3 - (2+7)) - 10/(5-3)");
        assertEquals("((3-(2+7))-(10/(5-3)))", source.get());
        assertEquals((3 - (2+7)) - 10/(5-3), ((3-(2+7))-(10/(5-3))));

        source = AttributeCompiler.translateToJavaGetter("(modelName.num - (2*(7-1))) - 10/(modelName.num-3)");
        assertEquals("((scope.modelName.getNum()-(2*(7-1)))-(10/(scope.modelName.getNum()-3)))", source.get());
    }

    @Test
    public void testStringCompilation(){
        assertEquals("\"Martin Luther King Jr.\"", AttributeCompiler.translateToJavaGetter("'Martin Luther King Jr.'").get());

        Option<String> getter = AttributeCompiler.translateToJavaGetter("xyz('Martin Luther King Jr.')");
        assertEquals("scope.xyz(\"Martin Luther King Jr.\")", getter.get());

        getter = AttributeCompiler.translateToJavaGetter("xyz('Martin Luther King\\'s Jr.')");
        assertEquals("scope.xyz(\"Martin Luther King\\'s Jr.\")", getter.get());


        getter = AttributeCompiler.translateToJavaGetter("xyz('Martin \"Luther\" King\\'s Jr.')");
        assertEquals("scope.xyz(\"Martin \\\"Luther\\\" King\\'s Jr.\")", getter.get());
    }

    @Test
    public void testStringAdditionCompilation(){
        Option<String> getter = AttributeCompiler.translateToJavaGetter("getStringValue() + 'orange ' + getIntValue()");
        assertEquals("((scope.getStringValue()+\"orange \")+scope.getIntValue())", getter.get());

        getter = AttributeCompiler.translateToJavaGetter("isTrue() ? 'abcdefg ' + getIntValue() : getStringValue()");
        assertEquals("(scope.isTrue())?((\"abcdefg \"+scope.getIntValue())):(scope.getStringValue())", getter.get());
    }

}