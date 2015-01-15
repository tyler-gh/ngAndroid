package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ngandroid.lib.parser.Tokenizer;

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

    public void testTokenizer(){

        Tokenizer tokenizer = new Tokenizer("testName.testField");
        Queue<Tokenizer.Token> tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 2);
        Tokenizer.Token token = tokenqueue.poll();
        System.out.println(token.getTokenType());
        assertTrue(token.getTokenType() == Tokenizer.TokenType.MODEL_NAME);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("testName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == Tokenizer.TokenType.MODEL_FIELD);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("testField"));


        tokenizer = new Tokenizer("functionName(parameter)");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 2);
        token = tokenqueue.poll();
        System.out.println(token.getTokenType());
        assertTrue(token.getTokenType() == Tokenizer.TokenType.FUNCTION_NAME);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == Tokenizer.TokenType.FUNCTION_PARAMETER);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("parameter"));

        tokenizer = new Tokenizer("functionName(parameter , parameter2  )");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 3);
        token = tokenqueue.poll();
        System.out.println(token.getTokenType());
        assertTrue(token.getTokenType() == Tokenizer.TokenType.FUNCTION_NAME);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == Tokenizer.TokenType.FUNCTION_PARAMETER);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == Tokenizer.TokenType.FUNCTION_PARAMETER);
        System.out.println(token.getScript());
        assertTrue(token.getScript().equals("parameter2"));




    }
}