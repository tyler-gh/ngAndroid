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

package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.interpreter.ExpressionBuilder;
import com.ngandroid.lib.annotations.Ignore;
import com.ngandroid.lib.ng.ModelBuilder;
import com.ngandroid.lib.ng.ModelBuilderMap;
import com.ngandroid.lib.utils.JsonUtils;

import org.json.JSONException;

import java.lang.reflect.Field;

/**
 * Created by tyler on 2/24/15.
 */
public class ModelTests extends ApplicationTestCase<Application> {

    Application testApplication;
    NgAndroid ngAndroid = NgAndroid.getInstance();

    public ModelTests() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        testApplication = getApplication();
    }

    public static interface TestSetterRequired{
        public int getX();
    }

    public static class TestScope {
        @Ignore
        private TestSetterRequired testSetterRequired;
        private TestGetterNotRequired testGetterNotRequired;
        private TestJsonModel testJsonModel;
        private TestSubModel testSubModel;
    }

    public void testBuildScope(){
        TestScope scope = ngAndroid.buildScope(TestScope.class);
        assertNull(scope.testSetterRequired);
        assertNotNull(scope.testGetterNotRequired);
        assertNotNull(scope.testJsonModel);
        assertNotNull(scope.testSubModel);
    }

    public void testSetterRequired(){

        try{
            ngAndroid.buildModel(TestSetterRequired.class);
            fail();
        }catch (Exception e){}
    }

    public static interface TestGetterNotRequired{
        public void setX(int x);
    }

    public void testGetterNotRequired(){
        try{
            ngAndroid.buildModel(TestGetterNotRequired.class);
        }catch (Exception e){
            fail();
        }
    }

    public static interface TestJsonModel{
        public int getInt();
        public void setInt(int i);
        public float getFloat();
        public void setFloat(float f);
        public double getDouble();
        public void setDouble(double d);
        public String getString();
        public void setString(String s);
        public boolean getBoolean();
        public void setBoolean(boolean b);
        public void setJsonModel(TestJsonModel tsm);
        public TestJsonModel getJsonModel();
    }

    public static interface TestSubModel{
        public void setJsonModel(TestJsonModel tsm);
        public TestJsonModel getJsonModel();
    }

    public void testBuildModelWithSubModels(){
        TestJsonModel testJsonModel = ngAndroid.buildModel(TestJsonModel.class);
        assertNull(testJsonModel.getJsonModel());

        TestSubModel testSubModel =  ngAndroid.buildModel(TestSubModel.class);
        assertNotNull(testSubModel.getJsonModel());
    }


    public void testBuildModelFromJson() throws JSONException {
        String json = "{ \"joe\" : \"xyz\", \"isInvisible\" : false, \"num\" : 10 }";
        ApplicationTest.TestModel model = JsonUtils.buildModelFromJson(json, ApplicationTest.TestModel.class);
        assertEquals("xyz", model.getJoe());
        assertEquals(false, model.getIsinvisible());
        assertEquals(10, model.getNum());

        json = "{ \"int\" : 25, \"float\" : 1.72, \"double\" : 0.0078 , \"string\" : \"string value\", \"boolean\" : false }";

        TestJsonModel jsonmodel = JsonUtils.buildModelFromJson(json, TestJsonModel.class);
        assertEquals(25, jsonmodel.getInt());
        assertEquals(1.72f, jsonmodel.getFloat());
        assertEquals(0.0078, jsonmodel.getDouble());
        assertEquals("string value", jsonmodel.getString());
        assertEquals(false, jsonmodel.getBoolean());

        json = "{ " +
                "\"int\" : 25, " +
                "\"float\" : 1.72, " +
                "\"double\" : 0.0078 , " +
                "\"string\" : \"string value\", " +
                "\"boolean\" : false, " +
                "\"jsonModel\" : {" +
                    "\"int\" : 25," +
                    "\"float\" : 1.72," +
                    "\"double\" : 894.378 ," +
                    "\"string\" : \"xyc\"," +
                    "\"boolean\" : false " +
                "}" +
        "}";

        jsonmodel = JsonUtils.buildModelFromJson(json, TestJsonModel.class);
        assertEquals(25, jsonmodel.getInt());
        assertEquals(1.72f, jsonmodel.getFloat());
        assertEquals(0.0078, jsonmodel.getDouble());
        assertEquals("string value", jsonmodel.getString());
        assertEquals(false, jsonmodel.getBoolean());
        assertEquals(25, jsonmodel.getJsonModel().getInt());
        assertEquals(1.72f, jsonmodel.getJsonModel().getFloat());
        assertEquals(894.378, jsonmodel.getJsonModel().getDouble());
        assertEquals("xyc", jsonmodel.getJsonModel().getString());
        assertEquals(false, jsonmodel.getJsonModel().getBoolean());

        jsonmodel = ngAndroid.modelFromJson(json, TestJsonModel.class);
        assertEquals(25, jsonmodel.getInt());
        assertEquals(1.72f, jsonmodel.getFloat());
        assertEquals(0.0078, jsonmodel.getDouble());
        assertEquals("string value", jsonmodel.getString());
        assertEquals(false, jsonmodel.getBoolean());
        assertEquals(25, jsonmodel.getJsonModel().getInt());
        assertEquals(1.72f, jsonmodel.getJsonModel().getFloat());
        assertEquals(894.378, jsonmodel.getJsonModel().getDouble());
        assertEquals("xyc", jsonmodel.getJsonModel().getString());
        assertEquals(false, jsonmodel.getJsonModel().getBoolean());
    }

    public void testDefaults() throws NoSuchFieldException, IllegalAccessException {
        TestScope tc = new TestScope();
        ModelBuilderMap map = new ModelBuilderMap(tc);
        tc.testJsonModel = NgAndroid.getInstance().buildModel(TestJsonModel.class);
        tc.testJsonModel.setInt(300);
        new ExpressionBuilder("testJsonModel.int").build(tc, map);
        Field f = tc.getClass().getDeclaredField("testJsonModel");
        f.setAccessible(true);
        assertNotNull(f.get(tc));
        assertTrue(tc.testJsonModel == f.get(tc));
        ModelBuilder.buildModel(tc, map);
        assertEquals(300, tc.testJsonModel.getInt());
    }


}
