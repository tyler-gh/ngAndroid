package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ngandroid.lib.interpreter.SyntaxParser;
import com.ngandroid.lib.interpreter.Token;
import com.ngandroid.lib.interpreter.TokenType;
import com.ngandroid.lib.interpreter.Tokenizer;

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
        assertEquals(TokenType.NUMBER_CONSTANT, token.getTokenType());
        assertEquals("234.453", token.getScript());
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
        assertTrue(token.getTokenType() == TokenType.NUMBER_CONSTANT);
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
        assertTrue(token.getTokenType() == TokenType.OPERATOR);
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
        assertTrue(token.getTokenType() == TokenType.OPERATOR);
        assertTrue(token.getScript().equals("=="));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.NUMBER_CONSTANT);
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
        assertTrue(token.getTokenType() == TokenType.OPERATOR);
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

    public void testBooleanExpressions() {
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
                    assertEquals(token.getTokenType(), TokenType.OPERATOR);
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
                    assertEquals(token.getTokenType(), TokenType.OPERATOR);
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
}