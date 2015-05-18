package com.github.davityle.ngprocessor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

/**
 * Created by tyler on 5/14/15.
 */
public class NestedClassesTest {

    @Test
    public void testNestedClasses(){
        String content = "" +
                "package com.github.davityle.ngprocessor;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope\n" +
                "public final class TestScope {\n" +
                "    @NgModel\n" +
                "    NestedModel nm;\n" +
                "    @NgModel\n" +
                "    NestedModel.NestedModel2 nm2;\n" +
                "\n" +
                "    @NgScope\n" +
                "    public static class NestedModel {\n" +
                "        @NgModel\n" +
                "        NestedModel2 nm2;\n" +
                "\n" +
                "        @NgScope\n" +
                "        public static class NestedModel2 {\n" +
                "            @NgModel\n" +
                "            NestedModel nm;\n" +
                "        }\n" +
                "    }\n" +
                "}";


        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.github.davityle.ngprocessor.TestScope", content))
                .processedWith(Collections.singletonList(new NgProcessor("ng-processor/src/test/resources/emptylayout")))
                .compilesWithoutError()
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "TestScope$$NgScope.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "TestScope$NestedModel$$NgScope.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "TestScope$NestedModel$NestedModel2$$NgScope.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "NestedModel$$NgModel.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "NestedModel2$$NgModel.java");
    }
}
