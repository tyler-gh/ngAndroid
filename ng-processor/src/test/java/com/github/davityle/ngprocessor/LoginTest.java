package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.Option;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Collections;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

public class LoginTest {

    @Test
    public void testLogin() {
        String content = "" +
        "package com.ngandroid.demo.scope;\n" +
        "\n" +
        "import com.ngandroid.lib.annotations.NgModel;\n" +
        "import com.ngandroid.lib.annotations.NgScope;\n" +
        "\n" +
        "@NgScope(name=\"Login\")\n" +
        "public class LoginScope {\n" +
        "\n" +
        "    @NgModel\n" +
        "    User user;\n" +
        "\n" +
        "    void onSubmit(android.view.View view){\n" +
        "    }\n" +
        "    void takesIntArgument(int arg) {}\n" +
        "\n" +
        "}\n" +
        "class User {\n" +
        "\n" +
        "    private String username = \"\", password = \"\";\n" +
        "\n" +
        "    public String getUsername() {\n" +
        "        return username;\n" +
        "    }\n" +
        "\n" +
        "    public void setUsername(String username) {\n" +
        "        this.username = username;\n" +
        "    }\n" +
        "\n" +
        "    public String getPassword() {\n" +
        "        return password;\n" +
        "    }\n" +
        "\n" +
        "    public void setPassword(String password) {\n" +
        "        this.password = password;\n" +
        "    }\n" +
        "}";
        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.ngandroid.demo.scope.LoginScope", content))
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/test_login"))))
                .compilesWithoutError();
    }

}
