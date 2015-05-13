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

package com.github.davityle.ngprocessor;

import com.google.common.io.Files;
import com.google.testing.compile.JavaFileObjects;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.annotation.processing.Processor;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

@NgScope
public class NgProcessorTest {

    private static Iterable<? extends Processor> ngProcessor() {
        return Arrays.asList(new NgProcessor());
    }

    @Test
    public void allTheThings() throws IOException {
        File file = new File("src/test/com/github/davityle/ngprocessor/NgProcessorTest.java");
        String content = Files.toString(file, StandardCharsets.UTF_8);

        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.github.davityle.ngprocessor.NgProcessorTest", content))
                .processedWith(ngProcessor())
                .compilesWithoutError();
    }

    @NgModel
    TestPoint point1, point2;


}

class TestPoint {
    private Double x;
    private Double y;

    private Float fx;
    private Float fy;

    private Integer ix;
    private Integer iy;

    private Short sy;
    private Short sx;

    private Byte by;
    private Byte bx;

    private Character cy;
    private Character cx;
}