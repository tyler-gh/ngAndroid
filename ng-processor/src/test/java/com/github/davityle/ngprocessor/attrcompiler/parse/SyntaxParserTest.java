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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxParserTest {
//
//    @Test
//    public void testFunctionTernaryParamater(){
//        SyntaxParser parser = new SyntaxParser("functionName(model.boolValue ? 'this string' : 'bool value was false')");
//        Token[] tokens = parser.parseScript();
//        int tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
//                    assertEquals(token.getScript(), "functionName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
//                    assertEquals(token.getScript(), "(");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "model");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "boolValue");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.TERNARY_QUESTION_MARK);
//                    assertEquals(token.getScript(), "?");
//                    break;
//                case 6:
//                    assertEquals(token.getTokenType(), TokenType.STRING);
//                    assertEquals(token.getScript(), "'this string'");
//                    break;
//                case 7:
//                    assertEquals(token.getTokenType(), TokenType.TERNARY_COLON);
//                    assertEquals(token.getScript(), ":");
//                    break;
//                case 8:
//                    assertEquals(token.getTokenType(), TokenType.STRING);
//                    assertEquals(token.getScript(), "'bool value was false'");
//                    break;
//                case 9:
//                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
//                    assertEquals(token.getScript(), ")");
//                    break;
//                case 10:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//    }
//
//    @Test
//    public void testModelAddition() {
//        SyntaxParser parser = new SyntaxParser("model.a + model.b");
//        Token[] tokens = parser.parseScript();
//        int tokenIndex = 0;
//        while(tokenIndex < tokens.length) {
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++) {
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "model");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "a");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.BINARY_OPERATOR);
//                    assertEquals(token.getScript(), "+");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "model");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 6:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "b");
//                    break;
//            }
//        }
//    }
//
//    @Test
//    public void testCorrectSyntax(){
//        SyntaxParser parser = new SyntaxParser("functionName(gui.parameter)");
//        Token[] tokens = parser.parseScript();
//        int tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
//                    assertEquals(token.getScript(), "functionName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
//                    assertEquals(token.getScript(), "(");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "gui");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "parameter");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
//                    assertEquals(token.getScript(), ")");
//                    break;
//                case 6:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//
//        parser = new SyntaxParser("functionName(pup.parameter , wep.secondparameter  )");
//        tokens = parser.parseScript();
//        tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
//                    assertEquals(token.getScript(), "functionName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
//                    assertEquals(token.getScript(), "(");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "pup");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "parameter");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.COMMA);
//                    assertEquals(token.getScript(), ",");
//                    break;
//                case 6:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "wep");
//                    break;
//                case 7:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 8:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "secondparameter");
//                    break;
//                case 9:
//                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
//                    assertEquals(token.getScript(), ")");
//                    break;
//                case 10:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//
//
//        parser = new SyntaxParser("modelName.boolValue ? functionName(kl.parameter , jl.secondparameter  ) : modelName.stringValue");
//        tokens = parser.parseScript();
//        tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "modelName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "boolValue");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.TERNARY_QUESTION_MARK);
//                    assertEquals(token.getScript(), "?");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
//                    assertEquals(token.getScript(), "functionName");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
//                    assertEquals(token.getScript(), "(");
//                    break;
//                case 6:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "kl");
//                    break;
//                case 7:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 8:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "parameter");
//                    break;
//                case 9:
//                    assertEquals(token.getTokenType(), TokenType.COMMA);
//                    assertEquals(token.getScript(), ",");
//                    break;
//                case 10:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "jl");
//                    break;
//                case 11:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 12:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "secondparameter");
//                    break;
//                case 13:
//                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
//                    assertEquals(token.getScript(), ")");
//                    break;
//                case 14:
//                    assertEquals(token.getTokenType(), TokenType.TERNARY_COLON);
//                    assertEquals(token.getScript(), ":");
//                    break;
//                case 15:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "modelName");
//                    break;
//                case 16:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 17:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "stringValue");
//                    break;
//                case 18:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//
//        parser = new SyntaxParser("testName.testField");
//        tokenIndex = 0;
//        tokens = parser.parseScript();
//        while(tokenIndex < tokens.length) {
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++) {
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "testName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "testField");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//
//        parser = new SyntaxParser("modelName.joe != 'orange'");
//        tokens = parser.parseScript();
//        tokenIndex = 0;
//        while(tokenIndex < tokens.length) {
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++) {
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "modelName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "joe");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.BINARY_OPERATOR);
//                    assertEquals(token.getScript(), "!=");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.STRING);
//                    assertEquals(token.getScript(), "'orange'");
//                    break;
//                case 5:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//
//        parser = new SyntaxParser("testName..testField");
//        try{
//            parser.parseScript();
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        parser = new SyntaxParser("testName.testField)");
//        try{
//            parser.parseScript();
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        parser = new SyntaxParser("testName(testField)(name)");
//        try{
//            parser.parseScript();
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        try{
//            new SyntaxParser("testName(\\testField)");
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        parser = new SyntaxParser(".stuffHere");
//        try{
//            parser.parseScript();
//            assertTrue(false);
//        }catch(Exception ignored){}
//
//        parser = new SyntaxParser("(stuffHere)");
//        try{
//            parser.parseScript();
//            assertTrue(false);
//        }catch(Exception ignored){}
//    }
//
//    @Test
//    public void testEmptyFunctionParse(){
//        SyntaxParser parser = new SyntaxParser("functionName()");
//        Token[] tokens = parser.parseScript();
//        int tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.FUNCTION_NAME);
//                    assertEquals(token.getScript(), "functionName");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.OPEN_PARENTHESIS);
//                    assertEquals(token.getScript(), "(");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.CLOSE_PARENTHESIS);
//                    assertEquals(token.getScript(), ")");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//    }
//
//    @Test
//    public void testFunctionNumberConstant(){
//        SyntaxParser parser = new SyntaxParser("multiply(input.test,2)");
//        parser.parseScript();
//    }
//
//    @Test
//    public void testBooleanExpressionSyntax() {
//        SyntaxParser parser = new SyntaxParser("!model.value");
//        Token[] tokens = parser.parseScript();
//        int tokenIndex = 0;
//        while(tokenIndex < tokens.length){
//            Token token = tokens[tokenIndex];
//            switch (tokenIndex++){
//                case 0:
//                    assertEquals(token.getTokenType(), TokenType.KNOT);
//                    assertEquals(token.getScript(), "!");
//                    break;
//                case 1:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_NAME);
//                    assertEquals(token.getScript(), "model");
//                    break;
//                case 2:
//                    assertEquals(token.getTokenType(), TokenType.PERIOD);
//                    assertEquals(token.getScript(), ".");
//                    break;
//                case 3:
//                    assertEquals(token.getTokenType(), TokenType.MODEL_FIELD);
//                    assertEquals(token.getScript(), "value");
//                    break;
//                case 4:
//                    assertEquals(token.getTokenType(), TokenType.EOF);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//    }
//
//    @Test
//    public void testNestedExpressions() throws Throwable {
//        SyntaxParser parser = new SyntaxParser("(3 + (2)) - 10/5");
//        Token[] tks = parser.parseScript();
//        assertEquals(12, tks.length);
//        assertEquals(TokenType.OPEN_PARENTHESIS_EXP, tks[0].getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tks[1].getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tks[2].getTokenType());
//        assertEquals(TokenType.OPEN_PARENTHESIS_EXP, tks[3].getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tks[4].getTokenType());
//        assertEquals(TokenType.CLOSE_PARENTHESIS, tks[5].getTokenType());
//        assertEquals(TokenType.CLOSE_PARENTHESIS, tks[6].getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tks[7].getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tks[8].getTokenType());
//        assertEquals(TokenType.BINARY_OPERATOR, tks[9].getTokenType());
//        assertEquals(TokenType.INTEGER_CONSTANT, tks[10].getTokenType());
//    }
}