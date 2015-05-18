package com.github.davityle.ngprocessor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by tyler on 5/15/15.
 */
public class InvalidModelTest {

    @Test
    public void testStringScope() {
        String content = "" +
                "package com.x.y;" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope\n" +
                "public final class StringScope {\n" +
                "    @NgModel\n" +
                "    android.view.View model;\n" +
                "}";

        JavaFileObject file = JavaFileObjects.forSourceString("com.x.y.StringScope", content);

        ASSERT.about(javaSource())
                .that(file)
                .processedWith(Collections.singletonList(new NgProcessor("ng-processor/src/test/resources/emptylayout")))
                .failsToCompile()
                .withErrorContaining("is missing a corresponding getter");
    }

}
