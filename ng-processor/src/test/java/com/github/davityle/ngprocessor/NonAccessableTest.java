package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.Option;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

/**
 * Created by tyler on 5/23/15.
 */
public class NonAccessableTest {


    @Test
    public void testPrivateUnusedMethod(){
        final String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope    \n" +
                "class PrivateScope {\n" +
                "   private void method(){\n" +
                "   }\n" +
                "}";

        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.yella.PrivateScope", content))
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .compilesWithoutError()
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.yella", "PrivateScope$$NgScope.java");
    }

    @Test
    public void testPrivateUsedMethod(){
        final String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope    \n" +
                "class PrivateScope {\n" +
                "   private void method(){\n" +
                "   }\n" +
                "}";

        JavaFileObject fileObject = JavaFileObjects.forSourceString("com.yella.PrivateScope", content);

        ASSERT.about(javaSource())
                .that(fileObject)
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/test_private_method"))))
                .failsToCompile()
                .withErrorContaining("is not accessible")
                .in(fileObject)
                .onLine(7);
    }

    @Test
    public void testPrivateModel(){
        final String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope    \n" +
                "class PrivateScope {\n" +
                "   @NgModel\n" +
                "   protected Model model;" +
                "}\n" +
                "class Model {}";

        JavaFileObject fileObject = JavaFileObjects.forSourceString("com.yella.PrivateScope", content);

        ASSERT.about(javaSource())
                .that(fileObject)
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .failsToCompile()
                .withErrorContaining("Unable to access field")
                .in(fileObject)
                .onLine(8);
    }

    @Test
    public void testPrivateModelValues(){
        final String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope    \n" +
                "class PrivateScope {\n" +
                "   @NgModel\n" +
                "   Model model;" +
                "}\n" +
                "class Model {\n" +
                "   private void setX(int x){\n" +
                "   }\n" +
                "   private int getX(){\n" +
                "       return 0;\n" +
                "   }\n" +
                "}";

        JavaFileObject fileObject = JavaFileObjects.forSourceString("com.yella.PrivateScope", content);

        ASSERT.about(javaSource())
                .that(fileObject)
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .failsToCompile()
                .withErrorContaining("Unable to access field")
                .in(fileObject)
                .onLine(10);
    }

    @Test
    public void testProtectedModelValues(){
        final String content = "" +
                "package com.yella;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope    \n" +
                "class PrivateScope {\n" +
                "   @NgModel\n" +
                "   Model model;" +
                "}\n" +
                "class Model {\n" +
                "   protected void setX(int x){\n" +
                "   }\n" +
                "   protected int getX(){\n" +
                "       return 0;\n" +
                "   }\n" +
                "}";

        JavaFileObject fileObject = JavaFileObjects.forSourceString("com.yella.PrivateScope", content);

        ASSERT.about(javaSource())
                .that(fileObject)
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .failsToCompile()
                .withErrorContaining("Unable to access field")
                .in(fileObject)
                .onLine(10);
    }

}
