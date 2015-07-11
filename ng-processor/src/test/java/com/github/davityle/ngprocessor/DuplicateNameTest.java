package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.Option;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by tyler on 5/20/15.
 */
public class DuplicateNameTest {

    @Test
    public void testDuplicateNames(){
        String content = "" +
                "package com.github.davityle.ngprocessor;\n" +
                "import com.ngandroid.lib.annotations.NgModel;\n" +
                "import com.ngandroid.lib.annotations.NgScope;\n\n" +
                "@NgScope\n" +
                "public final class DuplicateScope {\n" +
                "    \n" +
                "    @NgModel\n" +
                "    DuplicateModel dm;\n" +
                "\n" +
                "    public static class DuplicateModel {\n" +
                "        private String xml;\n" +
                "        private String XML;\n" +
                "\n" +
                "        public String getXml() {\n" +
                "            return xml;\n" +
                "        }\n" +
                "\n" +
                "        public void setXml(String xml) {\n" +
                "            this.xml = xml;\n" +
                "        }\n" +
                "\n" +
                "        public String getXML() {\n" +
                "            return XML;\n" +
                "        }\n" +
                "\n" +
                "        public void setXML(String XML) {\n" +
                "            this.XML = XML;\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JavaFileObject fileObject = JavaFileObjects.forSourceString("com.github.davityle.ngprocessor.DuplicateScope", content);

        ASSERT.about(javaSource())
                .that(fileObject)
                .processedWith((Iterable) Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/emptylayout"))))
                .failsToCompile()
                .withErrorContaining("Field 'XML' in model 'com.github.davityle.ngprocessor.DuplicateScope.DuplicateModel' is a duplicate.")
                .in(fileObject)
                .onLine(27);
    }
}
