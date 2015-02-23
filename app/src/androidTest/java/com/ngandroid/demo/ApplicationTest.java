package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.view.View;

import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.interpreter.ExpressionBuilder;
import com.ngandroid.lib.interpreter.SyntaxParser;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.interpreter.Tokenizer;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.ng.getters.BinaryOperatorGetter;
import com.ngandroid.lib.ng.getters.Getter;
import com.ngandroid.lib.ng.getters.KnotGetter;
import com.ngandroid.lib.ngattributes.ngclick.ClickInvoker;
import com.ngandroid.lib.ngattributes.ngif.NgDisabled;
import com.ngandroid.lib.ngattributes.ngif.NgGone;
import com.ngandroid.lib.ngattributes.ngif.NgInvisible;

import java.lang.reflect.Field;
import java.util.Queue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    Application testApplication;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        testApplication = getApplication();
    }

    public void testTokenizerNumbers(){
        Tokenizer tokenizer = new Tokenizer("234g");
        try {
            tokenizer.getTokens();
            assertTrue(false);
        }catch (Throwable ignored){}

        try {
            tokenizer = new Tokenizer("g234");
            tokenizer.getTokens();
            assertTrue(false);
        }catch (Throwable ignored){}

        tokenizer = new Tokenizer("234.453");
        Queue<Token> tokenqueue = tokenizer.getTokens();
        assertEquals(2, tokenqueue.size());

        Token token = tokenqueue.poll();
        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
        assertEquals("234.453", token.getScript());

        tokenizer = new Tokenizer("234l");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.LONG_CONSTANT, token.getTokenType());
        assertEquals("234l", token.getScript());


        tokenizer = new Tokenizer("234L");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.LONG_CONSTANT, token.getTokenType());
        assertEquals("234L", token.getScript());

        tokenizer = new Tokenizer("234f");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
        assertEquals("234f", token.getScript());

        tokenizer = new Tokenizer("234D");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.DOUBLE_CONSTANT, token.getTokenType());
        assertEquals("234D", token.getScript());

        tokenizer = new Tokenizer("234.0d");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.DOUBLE_CONSTANT, token.getTokenType());
        assertEquals("234.0d", token.getScript());

        tokenizer = new Tokenizer("234.0f");
        tokenqueue = tokenizer.getTokens();
        System.out.println(tokenqueue);
        assertEquals(2, tokenqueue.size());

        token = tokenqueue.poll();
        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
        assertEquals("234.0f", token.getScript());
    }

    public void testTokenizer(){

        Tokenizer tokenizer = new Tokenizer("testName.testField");
        Queue<Token> tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 4);
        Token token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("testName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("testField"));


        tokenizer = new Tokenizer("functionName(model.parameter)");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 7);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("model"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));



        tokenizer = new Tokenizer("functionName('string value here')");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 5);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.STRING);
        assertTrue(token.getScript().equals("'string value here'"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));


        tokenizer = new Tokenizer("functionName('string value here', 12345, model.xyz)");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 11);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.STRING);
        assertTrue(token.getScript().equals("'string value here'"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.INTEGER_CONSTANT);
        assertEquals(token.getScript(),"12345");

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("model"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("xyz"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));


        tokenizer = new Tokenizer("functionName(model.parameter , secondmodel.secondParameter  )");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 11);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("model"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("secondmodel"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("secondParameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));

        tokenizer = new Tokenizer(" modelName.boolValue?functionName(m.parameter , q.secondParameter  ) : modelName.stringValue");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 19);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("modelName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("boolValue"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.TERNARY_QUESTION_MARK);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("?"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("m"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("q"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("secondParameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.TERNARY_COLON);
        assertTrue(token.getScript().equals(":"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("modelName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("stringValue"));

        tokenizer = new Tokenizer(" modelName.joe + modelName.frank == 2");
        tokenqueue = tokenizer.getTokens();

        assertEquals(tokenqueue.toString(), 10, tokenqueue.size());

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("modelName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("joe"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
        assertTrue(token.getScript().equals("+"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("modelName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("frank"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
        assertTrue(token.getScript().equals("=="));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.INTEGER_CONSTANT);
        assertTrue(token.getScript().equals("2"));


        tokenizer = new Tokenizer(" modelName.joe != 'orange'");
        tokenqueue = tokenizer.getTokens();
        assertEquals(6, tokenqueue.size());

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
        assertTrue(token.getScript().equals("modelName"));
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.PERIOD);
        assertTrue(token.getScript().equals("."));
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
        assertTrue(token.getScript().equals("joe"));
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
        assertTrue(token.getScript().equals("!="));
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.STRING);
        assertTrue(token.getScript().equals("'orange'"));


        tokenizer = new Tokenizer("'this is a test string with \"quotes\" in it'");
        tokenqueue = tokenizer.getTokens();
        assertEquals(2, tokenqueue.size());
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.STRING);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("'this is a test string with \"quotes\" in it'"));

        tokenizer = new Tokenizer("model}thing");
        try {
            tokenizer.getTokens();
            assertTrue(false);
        }catch(Exception ignored){}

        tokenizer = new Tokenizer("\" this would be a string");
        try {
            tokenizer.getTokens();
            assertTrue(false);
        }catch(Exception ignored){}
    }

    public void testEmptyFunctionParse(){
        SyntaxParser parser = new SyntaxParser("functionName()");
        Token[] tokens = parser.parseScript();
        int tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
                    assertEquals(token.getScript(), "functionName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
                    assertEquals(token.getScript(), "(");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                    assertEquals(token.getScript(), ")");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }
    }

    public void testFunctionNumberConstant(){
        SyntaxParser parser = new SyntaxParser("multiply(input.test,2)");
        parser.parseScript();
    }

    public void testBooleanExpressionSyntax() {
        SyntaxParser parser = new SyntaxParser("!model.value");
        Token[] tokens = parser.parseScript();
        int tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.KNOT);
                    assertEquals(token.getScript(), "!");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "model");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "value");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }
    }

    public void testModelAddition() {
        SyntaxParser parser = new SyntaxParser("model.a + model.b");
        Token[] tokens = parser.parseScript();
        int tokenIndex = 0;
        while(tokenIndex < tokens.length) {
            Token token = tokens[tokenIndex];
            switch (tokenIndex++) {
                case 0:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "model");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "a");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.BINARY_OPERATOR);
                    assertEquals(token.getScript(), "+");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "model");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 6:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "b");
                    break;
            }
        }
    }

    public void testFunctionTernaryParamater(){
        SyntaxParser parser = new SyntaxParser("functionName(model.boolValue ? 'this string' : 'bool value was false')");
        Token[] tokens = parser.parseScript();
        int tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
                    assertEquals(token.getScript(), "functionName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
                    assertEquals(token.getScript(), "(");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "model");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "boolValue");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.TERNARY_QUESTION_MARK);
                    assertEquals(token.getScript(), "?");
                    break;
                case 6:
                    assertEquals(token.getTokenType(), TokenType.STRING);
                    assertEquals(token.getScript(), "'this string'");
                    break;
                case 7:
                    assertEquals(token.getTokenType(), TokenType.TERNARY_COLON);
                    assertEquals(token.getScript(), ":");
                    break;
                case 8:
                    assertEquals(token.getTokenType(), TokenType.STRING);
                    assertEquals(token.getScript(), "'bool value was false'");
                    break;
                case 9:
                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                    assertEquals(token.getScript(), ")");
                    break;
                case 10:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }
    }

    public void testSyntaxParser(){
        SyntaxParser parser = new SyntaxParser("functionName(gui.parameter)");
        Token[] tokens = parser.parseScript();
        int tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
                    assertEquals(token.getScript(), "functionName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
                    assertEquals(token.getScript(), "(");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "gui");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "parameter");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                    assertEquals(token.getScript(), ")");
                    break;
                case 6:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }

        parser = new SyntaxParser("functionName(pup.parameter , wep.secondparameter  )");
        tokens = parser.parseScript();
        tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
                    assertEquals(token.getScript(), "functionName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
                    assertEquals(token.getScript(), "(");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "pup");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "parameter");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.COMMA);
                    assertEquals(token.getScript(), ",");
                    break;
                case 6:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "wep");
                    break;
                case 7:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 8:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "secondparameter");
                    break;
                case 9:
                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                    assertEquals(token.getScript(), ")");
                    break;
                case 10:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }


        parser = new SyntaxParser("modelName.boolValue ? functionName(kl.parameter , jl.secondparameter  ) : modelName.stringValue");
        tokens = parser.parseScript();
        tokenIndex = 0;
        while(tokenIndex < tokens.length){
            Token token = tokens[tokenIndex];
            switch (tokenIndex++){
                case 0:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "modelName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "boolValue");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.TERNARY_QUESTION_MARK);
                    assertEquals(token.getScript(), "?");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
                    assertEquals(token.getScript(), "functionName");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
                    assertEquals(token.getScript(), "(");
                    break;
                case 6:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "kl");
                    break;
                case 7:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 8:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "parameter");
                    break;
                case 9:
                    assertEquals(token.getTokenType(), TokenType.COMMA);
                    assertEquals(token.getScript(), ",");
                    break;
                case 10:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "jl");
                    break;
                case 11:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 12:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "secondparameter");
                    break;
                case 13:
                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                    assertEquals(token.getScript(), ")");
                    break;
                case 14:
                    assertEquals(token.getTokenType(), TokenType.TERNARY_COLON);
                    assertEquals(token.getScript(), ":");
                    break;
                case 15:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "modelName");
                    break;
                case 16:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 17:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "stringValue");
                    break;
                case 18:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }

        parser = new SyntaxParser("testName.testField");
        tokenIndex = 0;
        tokens = parser.parseScript();
        while(tokenIndex < tokens.length) {
            Token token = tokens[tokenIndex];
            switch (tokenIndex++) {
                case 0:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "testName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "testField");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }

        parser = new SyntaxParser("modelName.joe != 'orange'");
        tokens = parser.parseScript();
        tokenIndex = 0;
        while(tokenIndex < tokens.length) {
            Token token = tokens[tokenIndex];
            switch (tokenIndex++) {
                case 0:
                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                    assertEquals(token.getScript(), "modelName");
                    break;
                case 1:
                    assertEquals(token.getTokenType(), TokenType.PERIOD);
                    assertEquals(token.getScript(), ".");
                    break;
                case 2:
                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                    assertEquals(token.getScript(), "joe");
                    break;
                case 3:
                    assertEquals(token.getTokenType(), TokenType.BINARY_OPERATOR);
                    assertEquals(token.getScript(), "!=");
                    break;
                case 4:
                    assertEquals(token.getTokenType(), TokenType.STRING);
                    assertEquals(token.getScript(), "'orange'");
                    break;
                case 5:
                    assertEquals(token.getTokenType(), TokenType.EOF);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }

        parser = new SyntaxParser("testName..testField");
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("testName.testField)");
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("testName(testField)(name)");
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        try{
            new SyntaxParser("testName(\\testField)");
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser(".stuffHere");
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("(stuffHere)");
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}
    }


    public static interface TestModel{
        public String getJoe();
        public void setJoe(String joe);
        public boolean getIsinvisible();
        public void setIsInvisible(boolean isVisible);
        public int getNum();
        public void setNum(int num);
    }

    public class TestScope {
        private TestModel modelName;
        private void method(){
        }
        private void method(String value){
            System.out.println(value);
        }
        private void method(int value){
            System.out.println(value);
        }
        private String getStringValue(){
            return "This is a string value ";
        }
        private int getIntValue(){
            return 42;
        }
        private boolean isTrue(){
            return true;
        }
    }


    public void testNumericAdditions() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("getIntValue() + 2 - 10/5");
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        Getter<Integer> getter = builder.build(tc, map);
        assertEquals(42, (int)getter.get());


        builder = new ExpressionBuilder("getIntValue() + 2 - 10/5.0");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        Getter<Float> fgetter = builder.build(tc, map);
        assertEquals(42.0f, fgetter.get());

        // this is  a serious issue....
        builder = new ExpressionBuilder("getIntValue() + 10/5.0 - 2");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        fgetter = builder.build(tc, map);
        assertEquals(42.0f, fgetter.get());

        builder = new ExpressionBuilder("getIntValue() + 1*5.0 - 2");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        fgetter = builder.build(tc, map);
        assertEquals(45.0f, fgetter.get());


        builder = new ExpressionBuilder("10d + 25f/5 - getIntValue() + 1*5.0 - 2");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        Getter<Double> dgetter = builder.build(tc, map);
        assertEquals(-24d, dgetter.get());

        builder = new ExpressionBuilder("10d + 25f/5 - getIntValue() + 1*5.0 - 2*modelName.num");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        dgetter = builder.build(tc, map);
        try {
            Field m = TestScope.class.getDeclaredField("modelName");
            m.setAccessible(true);
            m.set(tc, map.get("modelName").create());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
        tc.modelName.setNum(1);
        assertEquals(-24d, dgetter.get());
        tc.modelName.setNum(2);
        assertEquals(-26d, dgetter.get());
        tc.modelName.setNum(3);
        assertEquals(-28d, dgetter.get());
    }



    public void testStringAdditions() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("getStringValue() + 'orange ' + getIntValue()");
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        Getter<String> getter = builder.build(tc, map);
        assertEquals("This is a string value orange 42", getter.get());

        builder = new ExpressionBuilder("getStringValue() + getIntValue()");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        assertEquals("This is a string value 42", getter.get());

        builder = new ExpressionBuilder("getIntValue() + getStringValue()");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        assertEquals("42This is a string value ", getter.get());

        builder = new ExpressionBuilder("isTrue() ? 'abcdefg ' + getIntValue() : getStringValue()");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        assertEquals("abcdefg 42", getter.get());

        builder = new ExpressionBuilder("isTrue() ? getStringValue() + getIntValue() : getStringValue()");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        assertEquals("This is a string value 42", getter.get());

        builder = new ExpressionBuilder("modelName.joe + 'orange'");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        try {
            Field m = TestScope.class.getDeclaredField("modelName");
            m.setAccessible(true);
            m.set(tc, map.get("modelName").create());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
        tc.modelName.setJoe("joe");
        assertEquals("joeorange", getter.get());

        builder = new ExpressionBuilder("modelName.joe + getIntValue()");
        tc = new TestScope();
        map = new ModelBuilderMap(tc);
        getter = builder.build(tc, map);
        try {
            Field m = TestScope.class.getDeclaredField("modelName");
            m.setAccessible(true);
            m.set(tc, map.get("modelName").create());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
        tc.modelName.setJoe("joe");
        assertEquals("joe42", getter.get());
    }


    public void testExpressionBuilder() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("modelName.joe != 'orange'");
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        try {
            Field m = TestScope.class.getDeclaredField("modelName");
            m.setAccessible(true);
            m.set(tc, map.get("modelName").create());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Getter<Boolean> getter = builder.build(tc, map);
        System.out.println(getter.getClass().getSimpleName());
        assertTrue(getter instanceof BinaryOperatorGetter);
        tc.modelName.setJoe("Joe");
        System.out.println(tc.modelName.getJoe());
        assertTrue(getter.get());
        tc.modelName.setJoe("orange");
        assertFalse(getter.get());
    }



    public void testFunctionExpression() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("method()");
        TestScope tc = new TestScope();
        Getter<Boolean> getter = builder.build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof ClickInvoker);
        try {
            getter.get();
        }catch (Throwable e){
            assertTrue(false);
        }

    }

    public void testMethodTypeFinding() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("method('string')");
        TestScope tc = new TestScope();
        Getter<Boolean> getter = builder.build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof ClickInvoker);
        try {
            getter.get();
        }catch (Throwable e){
            assertTrue(false);
        }

        builder = new ExpressionBuilder("method(58)");
        getter = builder.build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof ClickInvoker);
        try {
            getter.get();
        }catch (Throwable e){
            assertTrue(false);
        }
    }

    public void testMethodInMethod() throws Throwable {
        ExpressionBuilder builder = new ExpressionBuilder("method(getStringValue())");
        TestScope tc = new TestScope();
        Getter<Boolean> getter = builder.build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof ClickInvoker);
        try {
            getter.get();
        }catch (Throwable e){
            assertTrue(false);
        }

        builder = new ExpressionBuilder("method('Int value = ' + getIntValue())");
        tc = new TestScope();
        getter = builder.build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof ClickInvoker);
        try {
            getter.get();
        }catch (Throwable e){
            assertTrue(false);
        }
    }

    public void testKnotExpression(){
        TestScope tc = new TestScope();
        Getter<Boolean> getter = new ExpressionBuilder("!isTrue()").build(tc, new ModelBuilderMap(tc));
        assertTrue(getter instanceof KnotGetter);
        try {
            assertFalse(getter.get());
        }catch (Throwable e){
            assertTrue(false);
        }
    }

    public void testNgVisible() throws Exception {
        View v = new View(testApplication);
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, map);
        NgInvisible.getInstance().attach(getter, map, v);
        ModelBuilder.buildModel(tc, map);
        tc.modelName.setIsInvisible(false);
        assertEquals(View.VISIBLE, v.getVisibility());
        tc.modelName.setIsInvisible(true);
        assertEquals(View.INVISIBLE, v.getVisibility());
    }

    public void testNgGone() throws Exception {
        View v = new View(testApplication);
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, map);
        NgGone.getInstance().attach(getter, map, v);
        ModelBuilder.buildModel(tc, map);
        tc.modelName.setIsInvisible(false);
        assertEquals(View.VISIBLE, v.getVisibility());
        tc.modelName.setIsInvisible(true);
        assertEquals(View.GONE, v.getVisibility());

        map = new ModelBuilderMap(tc);
        getter = new ExpressionBuilder("!modelName.isInvisible").build(tc, map);
        NgGone.getInstance().attach(getter, map, v);
        ModelBuilder.buildModel(tc, map);
        tc.modelName.setIsInvisible(true);
        assertEquals(View.VISIBLE, v.getVisibility());
        tc.modelName.setIsInvisible(false);
        assertEquals(View.GONE, v.getVisibility());

        map = new ModelBuilderMap(tc);
        getter = new ExpressionBuilder("!modelName.isInvisible ? isTrue() : !isTrue()").build(tc, map);
        NgGone.getInstance().attach(getter, map, v);
        ModelBuilder.buildModel(tc, map);
        tc.modelName.setIsInvisible(true);
        assertEquals(View.VISIBLE, v.getVisibility());
        tc.modelName.setIsInvisible(false);
        assertEquals(View.GONE, v.getVisibility());
    }

    public void testNgDisable() throws Exception {
        View v = new View(testApplication);
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, map);
        NgDisabled.getInstance().attach(getter, map, v);
        ModelBuilder.buildModel(tc, map);
        tc.modelName.setIsInvisible(false);
        assertTrue(v.isEnabled());
        tc.modelName.setIsInvisible(true);
        assertFalse(v.isEnabled());
    }

    public void testModelBuild(){
        TestScope tc = new TestScope();
        tc.modelName = NgAndroid.buildModel(TestModel.class);
        tc.modelName.setJoe("Frank");
        assertEquals("Frank", tc.modelName.getJoe());
    }


}