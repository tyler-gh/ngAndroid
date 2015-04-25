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

import android.test.ActivityInstrumentationTestCase2;

import com.ngandroid.demo.models.test.ModelTestScope;
import com.ngandroid.demo.ui.pages.main.DemoActivity;
import com.ngandroid.lib.NgAndroid;

/**
 * Created by tyler on 2/24/15.
 */
public class ModelTests extends ActivityInstrumentationTestCase2<DemoActivity> {

    NgAndroid ngAndroid = NgAndroid.getInstance();

    public ModelTests() {
        super(DemoActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBuildScope(){
        ModelTestScope scope = new ModelTestScope();
        ngAndroid.buildScope(scope);
        assertNull(scope.testSetterRequired);
        assertNotNull(scope.testJsonModel);
        assertNotNull(scope.testSubModel);
    }

//    public void testSetterRequired(){
//
//        try{
//            ngAndroid.buildModel(TestSetterRequired.class);
//            fail();
//        }catch (Exception e){}
//    }

    //    public void testGetterNotRequired(){
//        try{
//            ngAndroid.buildModel(TestGetterNotRequired.class);
//        }catch (Exception e){
//            fail();
//        }
//    }

    //    public void testBuildModelWithSubModels(){
//        TestJsonModel testJsonModel = ngAndroid.buildModel(TestJsonModel.class);
//        assertNull(testJsonModel.getJsonModel());
//
//        TestSubModel testSubModel =  ngAndroid.buildModel(TestSubModel.class);
//        assertNotNull(testSubModel.getJsonModel());
//    }
//
//
//    public void testBuildModelFromJson() throws JSONException {
//        String json = "{ \"joe\" : \"xyz\", \"isInvisible\" : false, \"num\" : 10 }";
//        ApplicationTest.TestModel model = JsonUtils.buildModelFromJson(json, ApplicationTest.TestModel.class);
//        assertEquals("xyz", model.getJoe());
//        assertEquals(false, model.getIsinvisible());
//        assertEquals(10, model.getNum());
//
//        json = "{ \"int\" : 25, \"float\" : 1.72, \"double\" : 0.0078 , \"string\" : \"string value\", \"boolean\" : false }";
//
//        TestJsonModel jsonmodel = JsonUtils.buildModelFromJson(json, TestJsonModel.class);
//        assertEquals(25, jsonmodel.getInt());
//        assertEquals(1.72f, jsonmodel.getFloat());
//        assertEquals(0.0078, jsonmodel.getDouble());
//        assertEquals("string value", jsonmodel.getString());
//        assertEquals(false, jsonmodel.getBoolean());
//
//        json = "{ " +
//                "\"int\" : 25, " +
//                "\"float\" : 1.72, " +
//                "\"double\" : 0.0078 , " +
//                "\"string\" : \"string value\", " +
//                "\"boolean\" : false, " +
//                "\"jsonModel\" : {" +
//                    "\"int\" : 25," +
//                    "\"float\" : 1.72," +
//                    "\"double\" : 894.378 ," +
//                    "\"string\" : \"xyc\"," +
//                    "\"boolean\" : false " +
//                "}" +
//        "}";
//
//        jsonmodel = JsonUtils.buildModelFromJson(json, TestJsonModel.class);
//        assertEquals(25, jsonmodel.getInt());
//        assertEquals(1.72f, jsonmodel.getFloat());
//        assertEquals(0.0078, jsonmodel.getDouble());
//        assertEquals("string value", jsonmodel.getString());
//        assertEquals(false, jsonmodel.getBoolean());
//        assertEquals(25, jsonmodel.getJsonModel().getInt());
//        assertEquals(1.72f, jsonmodel.getJsonModel().getFloat());
//        assertEquals(894.378, jsonmodel.getJsonModel().getDouble());
//        assertEquals("xyc", jsonmodel.getJsonModel().getString());
//        assertEquals(false, jsonmodel.getJsonModel().getBoolean());
//
//        jsonmodel = ngAndroid.modelFromJson(json, TestJsonModel.class);
//        assertEquals(25, jsonmodel.getInt());
//        assertEquals(1.72f, jsonmodel.getFloat());
//        assertEquals(0.0078, jsonmodel.getDouble());
//        assertEquals("string value", jsonmodel.getString());
//        assertEquals(false, jsonmodel.getBoolean());
//        assertEquals(25, jsonmodel.getJsonModel().getInt());
//        assertEquals(1.72f, jsonmodel.getJsonModel().getFloat());
//        assertEquals(894.378, jsonmodel.getJsonModel().getDouble());
//        assertEquals("xyc", jsonmodel.getJsonModel().getString());
//        assertEquals(false, jsonmodel.getJsonModel().getBoolean());
//    }
//    @UiThreadTest
//    public void testDefaults() throws NoSuchFieldException, IllegalAccessException {
//        ModelTestScope tc = new ModelTestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        tc.testJsonModel.setInt(300);
//        new ExpressionBuilder("testJsonModel.int").build(tc, scope);
//        Field f = tc.getClass().getDeclaredField("testJsonModel");
//        f.setAccessible(true);
//        assertNotNull(f.get(tc));
//        assertTrue(tc.testJsonModel == f.get(tc));
//        assertEquals(300, tc.testJsonModel.getInt());
//    }


}
