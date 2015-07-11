package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.Option;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

public class DuplicateModelNameTest {

    @Test
    public void testDuplicateModelNames(){
        String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope\n" +
                "public class DuplicateModelScopeTest {\n" +
                "    @NgModel\n" +
                "    DupModel dupModel, dupModeL;        \n" +
                "}\n" +
                "\n" +
                "interface DupModel {\n" +
                "    void setX(int x);\n" +
                "    int getX();\n" +
                "}";
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.yella.DuplicateModelScopeTest", content))
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .compilesWithoutError()
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.yella", "DuplicateModelScopeTest$$NgScope.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.yella", "DupModel$$NgModel.java");
    }

}
