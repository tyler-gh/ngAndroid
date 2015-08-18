/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.davityle.ngprocessor.attrcompiler.parse;

public class TokenizerTest {

//    @Test
//    public void testTokenizerNumbers(){
//        Tokenizer tokenizer = new Tokenizer("234g");
//        try {
//            tokenizer.getTokens();
//            assertTrue(false);
//        }catch (Throwable ignored){}
//
//        try {
//            tokenizer = new Tokenizer("g234");
//            tokenizer.getTokens();
//            assertTrue(false);
//        }catch (Throwable ignored){}
//
//        tokenizer = new Tokenizer("234.453");
//        Queue<Token> tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        Token token = tokenqueue.poll();
//        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
//        assertEquals("234.453", token.getScript());
//
//        tokenizer = new Tokenizer("234l");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertEquals(TokenType.LONG_CONSTANT, token.getTokenType());
//        assertEquals("234l", token.getScript());
//
//
//        tokenizer = new Tokenizer("234L");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertEquals(TokenType.LONG_CONSTANT, token.getTokenType());
//        assertEquals("234L", token.getScript());
//
//        tokenizer = new Tokenizer("234f");
//        tokenqueue = tokenizer.getTokens();
//
//        assertEquals(2, tokenqueue.size());
//        token = tokenqueue.poll();
//        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
//        assertEquals("234f", token.getScript());
//
//        tokenizer = new Tokenizer("234D");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertEquals(TokenType.DOUBLE_CONSTANT, token.getTokenType());
//        assertEquals("234D", token.getScript());
//
//        tokenizer = new Tokenizer("234.0d");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertEquals(TokenType.DOUBLE_CONSTANT, token.getTokenType());
//        assertEquals("234.0d", token.getScript());
//
//        tokenizer = new Tokenizer("234.0f");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertEquals(TokenType.FLOAT_CONSTANT, token.getTokenType());
//        assertEquals("234.0f", token.getScript());
//    }
//
//    @Test
//    public void testTokenizer(){
//
//        Tokenizer tokenizer = new Tokenizer("testName.testField");
//        Queue<Token> tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 4);
//        Token token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("testName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("testField"));
//
//
//        tokenizer = new Tokenizer("functionName(model.parameter)");
//        tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 7);
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
//        assertTrue(token.getScript().equals("functionName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
//        assertTrue(token.getScript().equals("("));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("model"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("parameter"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
//        assertTrue(token.getScript().equals(")"));
//
//
//
//        tokenizer = new Tokenizer("functionName('string value here')");
//        tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 5);
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
//        assertTrue(token.getScript().equals("functionName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
//        assertTrue(token.getScript().equals("("));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.STRING);
//        assertTrue(token.getScript().equals("'string value here'"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
//        assertTrue(token.getScript().equals(")"));
//
//
//        tokenizer = new Tokenizer("functionName('string value here', 12345, model.xyz)");
//        tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 11);
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
//        assertTrue(token.getScript().equals("functionName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
//        assertTrue(token.getScript().equals("("));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.STRING);
//        assertTrue(token.getScript().equals("'string value here'"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.COMMA);
//        assertTrue(token.getScript().equals(","));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.INTEGER_CONSTANT);
//        assertEquals(token.getScript(),"12345");
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.COMMA);
//        assertTrue(token.getScript().equals(","));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("model"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("xyz"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
//        assertTrue(token.getScript().equals(")"));
//
//
//        tokenizer = new Tokenizer("functionName(model.parameter , secondmodel.secondParameter  )");
//        tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 11);
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
//        assertTrue(token.getScript().equals("functionName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
//        assertTrue(token.getScript().equals("("));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("model"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("parameter"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.COMMA);
//        assertTrue(token.getScript().equals(","));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("secondmodel"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("secondParameter"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
//        assertTrue(token.getScript().equals(")"));
//
//        tokenizer = new Tokenizer(" modelName.boolValue?functionName(m.parameter , q.secondParameter  ) : modelName.stringValue");
//        tokenqueue = tokenizer.getTokens();
//
//        assertTrue(tokenqueue.size() == 19);
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("modelName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("boolValue"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.TERNARY_QUESTION_MARK);
//        assertTrue(token.getScript().equals("?"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.FUNCTION_NAME);
//        assertTrue(token.getScript().equals("functionName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.OPEN_PARENTHESIS);
//        assertTrue(token.getScript().equals("("));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("m"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("parameter"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.COMMA);
//        assertTrue(token.getScript().equals(","));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("q"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("secondParameter"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.CLOSE_PARENTHESIS);
//        assertTrue(token.getScript().equals(")"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.TERNARY_COLON);
//        assertTrue(token.getScript().equals(":"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("modelName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("stringValue"));
//
//        tokenizer = new Tokenizer(" modelName.joe + modelName.frank == 2");
//        tokenqueue = tokenizer.getTokens();
//
//        assertEquals(tokenqueue.toString(), 10, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("modelName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("joe"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
//        assertTrue(token.getScript().equals("+"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("modelName"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("frank"));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
//        assertTrue(token.getScript().equals("=="));
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.INTEGER_CONSTANT);
//        assertTrue(token.getScript().equals("2"));
//
//
//        tokenizer = new Tokenizer(" modelName.joe != 'orange'");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(6, tokenqueue.size());
//
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_NAME);
//        assertTrue(token.getScript().equals("modelName"));
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.PERIOD);
//        assertTrue(token.getScript().equals("."));
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.MODEL_FIELD);
//        assertTrue(token.getScript().equals("joe"));
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.BINARY_OPERATOR);
//        assertTrue(token.getScript().equals("!="));
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.STRING);
//        assertTrue(token.getScript().equals("'orange'"));
//
//
//        tokenizer = new Tokenizer("'this is a test string with \"quotes\" in it'");
//        tokenqueue = tokenizer.getTokens();
//        assertEquals(2, tokenqueue.size());
//        token = tokenqueue.poll();
//        assertTrue(token.getTokenType() == TokenType.STRING);
//        assertTrue(token.getScript().equals("'this is a test string with \"quotes\" in it'"));
//
//        tokenizer = new Tokenizer("model}thing");
//        try {
//            tokenizer.getTokens();
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        tokenizer = new Tokenizer("\" this would be a string");
//        try {
//            tokenizer.getTokens();
//            assertTrue(false);
//        }catch(Exception ignored){}
//    }
//
//    @Test
//    public void testStringSlash(){
//        Tokenizer tokenizer = new Tokenizer("'\\''");
//        Queue<Token> tokens = tokenizer.getTokens();
//        assertEquals(2, tokens.size());
//        assertEquals("'''", tokens.poll().getScript());
//    }
//
//
//    @Test
//    public void testNestedExpressions() throws Throwable {
//
//        Tokenizer tokenizer = new Tokenizer("(3 + 2) - 10/5");
//        Queue<Token> tokens = tokenizer.getTokens();
//        assertEquals(10, tokens.size());
//        assertEquals(TokenType.OPEN_PARENTHESIS_EXP, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//
//        tokenizer = new Tokenizer("(3 + (2)) - 10/5");
//        tokens = tokenizer.getTokens();
//
//        assertEquals(12, tokens.size());
//        assertEquals(TokenType.OPEN_PARENTHESIS_EXP, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.OPEN_PARENTHESIS_EXP, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens.poll().getTokenType());
//        assertEquals(TokenType.CLOSE_PARENTHESIS, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tokens.poll().getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tokens.poll().getTokenType());
//    }
}