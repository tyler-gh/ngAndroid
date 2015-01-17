package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ngandroid.lib.parser.SyntaxParser;
import com.ngandroid.lib.parser.Token;
import com.ngandroid.lib.parser.TokenType;
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

    public void testTokenizerNumbers(){

        Tokenizer tokenizer = new Tokenizer("234g");
        Queue<Token> tokenqueue = tokenizer.getTokens();
        assertTrue("tokenqueue.size() == " + tokenqueue.size(), tokenqueue.size() == 3);

        Token token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.NUMBER_CONSTANT);
        assertTrue(token.getScript().equals("234"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.RUBBISH);
        assertTrue(token.getScript().equals("g"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.EOF);

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


        tokenizer = new Tokenizer("functionName(parameter)");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 5);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_PARAMETER);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));

        tokenizer = new Tokenizer("functionName(parameter , secondParameter  )");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 7);
        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
        assertTrue(token.getScript().equals("functionName"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
        assertTrue(token.getScript().equals("("));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_PARAMETER);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_PARAMETER);
        assertTrue(token.getScript().equals("secondParameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
        assertTrue(token.getScript().equals(")"));

        tokenizer = new Tokenizer(" modelName.boolValue?functionName(parameter , secondParameter  ) : modelName.stringValue");
        tokenqueue = tokenizer.getTokens();

        assertTrue(tokenqueue.size() == 15);
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
        assertTrue(token.getTokenType() == TokenType.FUNCTION_PARAMETER);
        assertTrue(token.getScript().equals("parameter"));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.COMMA);
        assertTrue(token.getScript().equals(","));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.FUNCTION_PARAMETER);
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
        for(Token t : tokenqueue){
            System.out.println(t);
        }

        assertEquals(10, tokenqueue.size());

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
        assertTrue(token.getTokenType() == TokenType.ADDITION);
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
        assertTrue(token.getTokenType() == TokenType.EQUALS_EQUALS);
        assertTrue(token.getScript().equals("=="));

        token = tokenqueue.poll();
        assertTrue(token.getTokenType() == TokenType.NUMBER_CONSTANT);
        assertTrue(token.getScript().equals("2"));



        tokenizer = new Tokenizer("model}thing");
        try {
            tokenizer.getTokens();
            assertTrue(false);
        }catch(Exception ignored){}
    }

    public void testSyntaxParser(){
        SyntaxParser parser = new SyntaxParser("functionName(parameter)", new SyntaxParser.TokenConsumer() {
            int tokenIndex = 0;
            @Override
            public void OnValidToken(Token token) {
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
                        assertEquals(token.getTokenType(), TokenType.FUNCTION_PARAMETER);
                        assertEquals(token.getScript(), "parameter");
                        break;
                    case 3:
                        assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                        assertEquals(token.getScript(), ")");
                        break;
                    case 4:
                        assertEquals(token.getTokenType(), TokenType.EOF);
                        break;
                    default:
                        assertTrue(false);
                        break;
                }
            }
        });
        parser.parseScript();

        parser = new SyntaxParser("functionName(parameter , secondparameter  )", new SyntaxParser.TokenConsumer() {
            int tokenIndex = 0;
            @Override
            public void OnValidToken(Token token) {
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
                        assertEquals(token.getTokenType(), TokenType.FUNCTION_PARAMETER);
                        assertEquals(token.getScript(), "parameter");
                        break;
                    case 3:
                        assertEquals(token.getTokenType(), TokenType.COMMA);
                        assertEquals(token.getScript(), ",");
                        break;
                    case 4:
                        assertEquals(token.getTokenType(), TokenType.FUNCTION_PARAMETER);
                        assertEquals(token.getScript(), "secondparameter");
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
        });
        parser.parseScript();


        parser = new SyntaxParser("modelName.boolValue ? functionName(parameter , secondparameter  ) : modelName.stringValue", new SyntaxParser.TokenConsumer() {
            int tokenIndex = 0;
            @Override
            public void OnValidToken(Token token) {
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
                        assertEquals(token.getTokenType(), TokenType.FUNCTION_PARAMETER);
                        assertEquals(token.getScript(), "parameter");
                        break;
                    case 7:
                        assertEquals(token.getTokenType(), TokenType.COMMA);
                        assertEquals(token.getScript(), ",");
                        break;
                    case 8:
                        assertEquals(token.getTokenType(), TokenType.FUNCTION_PARAMETER);
                        assertEquals(token.getScript(), "secondparameter");
                        break;
                    case 9:
                        assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
                        assertEquals(token.getScript(), ")");
                        break;
                    case 10:
                        assertEquals(token.getTokenType(), TokenType.TERNARY_COLON);
                        assertEquals(token.getScript(), ":");
                        break;
                    case 11:
                        assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
                        assertEquals(token.getScript(), "modelName");
                        break;
                    case 12:
                        assertEquals(token.getTokenType(), TokenType.PERIOD);
                        assertEquals(token.getScript(), ".");
                        break;
                    case 13:
                        assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
                        assertEquals(token.getScript(), "stringValue");
                        break;
                    case 14:
                        assertEquals(token.getTokenType(), TokenType.EOF);
                        break;
                    default:
                        assertTrue(false);
                        break;
                }
            }
        });
        parser.parseScript();


        parser = new SyntaxParser("testName.testField", new SyntaxParser.TokenConsumer() {
            int tokenIndex = 0;
            @Override
            public void OnValidToken(Token token) {
                switch (tokenIndex++){
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
        });
        parser.parseScript();

        SyntaxParser.TokenConsumer emptyConsumer = new SyntaxParser.TokenConsumer(){ @Override public void OnValidToken(Token token) {}};

        parser = new SyntaxParser("testName..testField", emptyConsumer);
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("testName.testField)", emptyConsumer);
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("testName(testField)(name)", emptyConsumer);
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        try{
            new SyntaxParser("testName(\\testField)", emptyConsumer);
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser(".stuffHere", emptyConsumer);
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}

        parser = new SyntaxParser("(stuffHere)", emptyConsumer);
        try{
            parser.parseScript();
            assertTrue(false);
        }catch(Exception ignored){}
    }
}