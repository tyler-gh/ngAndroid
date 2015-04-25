package com.ngandroid.demo;

import android.app.Application;
import android.test.ApplicationTestCase;

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


//    public void testNumericAdditions() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("getIntValue() + 2 - 10/5");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Integer> getter = builder.build(tc, scope);
//        assertEquals(42, (int)getter.get());
//
//
//        builder = new ExpressionBuilder("getIntValue() + 2 - 10/5.0");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        Getter<Float> fgetter = builder.build(tc, scope);
//        assertEquals(42.0f, fgetter.get());
//
//        builder = new ExpressionBuilder("getIntValue() + 10/5.0 - 2");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        fgetter = builder.build(tc, scope);
//        assertEquals(42.0f, fgetter.get());
//
//        builder = new ExpressionBuilder("getIntValue() + 1*5.0 - 2");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        fgetter = builder.build(tc, scope);
//        assertEquals(45.0f, fgetter.get());
//
//
//        builder = new ExpressionBuilder("10d + 25f/5 - getIntValue() + 1*5.0 - 2");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        Getter<Double> dgetter = builder.build(tc, scope);
//        assertEquals(-24d, dgetter.get());
//
//        builder = new ExpressionBuilder("10d + 25f/5 - getIntValue() + 1*5.0 - 2*modelName.num");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        dgetter = builder.build(tc, scope);
//        tc.modelName.setNum(1);
//        assertEquals(-24d, dgetter.get());
//        tc.modelName.setNum(2);
//        assertEquals(-26d, dgetter.get());
//        tc.modelName.setNum(3);
//        assertEquals(-28d, dgetter.get());
//    }
//
//
//
//    public void testStringAdditions() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("getStringValue() + 'orange ' + getIntValue()");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<String> getter = builder.build(tc, scope);
//        assertEquals("This is a string value orange 42", getter.get());
//
//        builder = new ExpressionBuilder("getStringValue() + getIntValue()");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        assertEquals("This is a string value 42", getter.get());
//
//        builder = new ExpressionBuilder("getIntValue() + getStringValue()");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        assertEquals("42This is a string value ", getter.get());
//
//        builder = new ExpressionBuilder("isTrue() ? 'abcdefg ' + getIntValue() : getStringValue()");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        assertEquals("abcdefg 42", getter.get());
//
//        builder = new ExpressionBuilder("isTrue() ? getStringValue() + getIntValue() : getStringValue()");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        assertEquals("This is a string value 42", getter.get());
//
//        builder = new ExpressionBuilder("modelName.joe + 'orange'");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        tc.modelName.setJoe("joe");
//        assertEquals("joeorange", getter.get());
//
//        builder = new ExpressionBuilder("modelName.joe + getIntValue()");
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = builder.build(tc, scope);
//        tc.modelName.setJoe("joe");
//        assertEquals("joe42", getter.get());
//    }
//
//
//    public void testExpressionBuilder() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("modelName.joe != 'orange'");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = builder.build(tc, scope);
//        System.out.println(getter.getClass().getSimpleName());
//        assertTrue(getter instanceof BinaryOperatorGetter);
//        tc.modelName.setJoe("Joe");
//        System.out.println(tc.modelName.getJoe());
//        assertTrue(getter.get());
//        tc.modelName.setJoe("orange");
//        assertFalse(getter.get());
//    }
//
//
//
//    public void testFunctionExpression() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("method()");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = builder.build(tc, scope);
//        assertTrue(getter instanceof MethodGetter);
//        try {
//            getter.get();
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//
//    }
//
//    public void testMethodTypeFinding() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("method('string')");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = builder.build(tc, scope);
//        assertTrue(getter instanceof MethodGetter);
//        try {
//            getter.get();
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//
//        builder = new ExpressionBuilder("method(58)");
//        getter = builder.build(tc, scope);
//        assertTrue(getter instanceof MethodGetter);
//        try {
//            getter.get();
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//    }
//
//    public void testMethodInMethod() throws Throwable {
//        ExpressionBuilder builder = new ExpressionBuilder("method(getStringValue())");
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = builder.build(tc, scope);
//        assertTrue(getter instanceof MethodGetter);
//        try {
//            getter.get();
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//
//        builder = new ExpressionBuilder("method('Int value = ' + getIntValue())");
//        tc = new TestScope();
//        getter = builder.build(tc, scope);
//        assertTrue(getter instanceof MethodGetter);
//        try {
//            getter.get();
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//    }
//
//    public void testKnotExpression(){
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = new ExpressionBuilder("!isTrue()").build(tc, scope);
//        assertTrue(getter instanceof KnotGetter);
//        try {
//            assertFalse(getter.get());
//        }catch (Throwable e){
//            assertTrue(false);
//        }
//    }
//
//    public void testNgVisible() throws Throwable {
//        View v = new View(testApplication);
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, scope);
//        ModelGetter[] modelGetters = ModelGetter.getModelGetters(getter);
//        Model[] modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgInvisible.getInstance().attach(getter, v, modelGetters, modelBuilders, );
//        tc.modelName.setIsInvisible(false);
//        assertEquals(View.VISIBLE, v.getVisibility());
//        tc.modelName.setIsInvisible(true);
//        assertEquals(View.INVISIBLE, v.getVisibility());
//    }
//
//    public void testNgText() throws Throwable {
//        TextView v = new TextView(testApplication);
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = new ExpressionBuilder("modelName.joe").build(tc, scope);
//        ModelGetter[] modelGetters = ModelGetter.getModelGetters(getter);
//        Model[] modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgText.getInstance().attach(getter, modelGetters, modelBuilders, v);
//        tc.modelName.setJoe("This is Joe");
//        assertEquals(v.getText(), "This is Joe");
//    }
//
//    public void testNgGone() throws Throwable {
//        View v = new View(testApplication);
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, scope);
//        ModelGetter[] modelGetters = ModelGetter.getModelGetters(getter);
//        Model[] modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgGone.getInstance().attach(getter, v, modelGetters, modelBuilders, );
//        tc.modelName.setIsInvisible(false);
//        assertEquals(View.VISIBLE, v.getVisibility());
//        tc.modelName.setIsInvisible(true);
//        assertEquals(View.GONE, v.getVisibility());
//
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = new ExpressionBuilder("!modelName.isInvisible").build(tc, scope);
//        modelGetters = ModelGetter.getModelGetters(getter);
//        modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgGone.getInstance().attach(getter, v, modelGetters, modelBuilders, );
//        tc.modelName.setIsInvisible(true);
//        assertEquals(View.VISIBLE, v.getVisibility());
//        tc.modelName.setIsInvisible(false);
//        assertEquals(View.GONE, v.getVisibility());
//
//        tc = new TestScope();
//        scope = ScopeBuilder.buildScope(tc);
//        getter = new ExpressionBuilder("!modelName.isInvisible ? isTrue() : !isTrue()").build(tc, scope);
//        modelGetters = ModelGetter.getModelGetters(getter);
//        modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgGone.getInstance().attach(getter, v, modelGetters, modelBuilders, );
//        tc.modelName.setIsInvisible(true);
//        assertEquals(View.VISIBLE, v.getVisibility());
//        tc.modelName.setIsInvisible(false);
//        assertEquals(View.GONE, v.getVisibility());
//    }
//
//    public void testNgDisable() throws Throwable {
//        View v = new View(testApplication);
//        TestScope tc = new TestScope();
//        Scope scope = ScopeBuilder.buildScope(tc);
//        Getter<Boolean> getter = new ExpressionBuilder("modelName.isInvisible").build(tc, scope);
//        ModelGetter[] modelGetters = ModelGetter.getModelGetters(getter);
//        Model[] modelBuilders = ModelGetter.getModels(modelGetters, scope);
//        NgDisabled.getInstance().attach(getter, v, modelGetters, modelBuilders, );
//        tc.modelName.setIsInvisible(false);
//        assertTrue(v.isEnabled());
//        tc.modelName.setIsInvisible(true);
//        assertFalse(v.isEnabled());
//    }

}