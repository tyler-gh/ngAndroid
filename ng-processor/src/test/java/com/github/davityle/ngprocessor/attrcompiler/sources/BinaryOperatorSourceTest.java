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

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryOperatorSourceTest {

    @Test
    public void testOperatorTypeResults(){

        Object a = 1l + 1;
        assertTrue(a instanceof Long);
        assertNotEquals(2, a);
        assertEquals(2l, a);

        Object b = "" + 3.14159;

        assertTrue(b instanceof String);
        assertEquals("3.14159", b);
    }

}