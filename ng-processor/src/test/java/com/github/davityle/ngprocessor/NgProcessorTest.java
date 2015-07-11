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

package com.github.davityle.ngprocessor;

import com.github.davityle.ngprocessor.util.Option;
import com.google.common.io.Files;
import com.google.testing.compile.JavaFileObjects;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

@NgScope
public class NgProcessorTest {

    @Test
    public void boxedTypesTest() throws IOException {

        File file = new File("./ng-processor/src/test/java/com/github/davityle/ngprocessor/NgProcessorTest.java");
        String content = Files.toString(file, StandardCharsets.UTF_8);

        ASSERT.about(javaSource())
                .that(JavaFileObjects.forSourceString("com.github.davityle.ngprocessor.NgProcessorTest", content))
                .processedWith(Collections.singletonList(new NgProcessor(Option.of("ng-processor/src/test/resources/layouts"))))
                .compilesWithoutError()
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "NgProcessorTest$$NgScope.java")
                .and()
                .generatesFileNamed(SOURCE_OUTPUT, "com.github.davityle.ngprocessor", "TestPoint$$NgModel.java");
    }

    @NgModel
    TestPoint point1, point2;

    void doSomething(Double x, Double y){

    }

    int addCharWithByte(Character x, Byte y){
        return x + y;
    }

    int passInt(Integer y){
        return y;
    }

    void stringAddition(String str){

    }

    void floatPlusDouble(Double x){

    }

    void floatPlusChar(Float x){

    }
}

/**
 * for testing Boxed types
 */
class TestPoint {
    private Double x;
    private Double y;

    private Float fx;
    private Float fy;

    private Integer ix;
    private Integer iy;

    private Short sy;
    private Short sx;

    private Byte by;
    private Byte bx;

    private Character cy;
    private Character cx;

    private String str;

    private boolean bool;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Float getFx() {
        return fx;
    }

    public void setFx(Float fx) {
        this.fx = fx;
    }

    public Float getFy() {
        return fy;
    }

    public void setFy(Float fy) {
        this.fy = fy;
    }

    public Integer getIx() {
        return ix;
    }

    public void setIx(Integer ix) {
        this.ix = ix;
    }

    public Integer getIy() {
        return iy;
    }

    public void setIy(Integer iy) {
        this.iy = iy;
    }

    public Short getSy() {
        return sy;
    }

    public void setSy(Short sy) {
        this.sy = sy;
    }

    public Short getSx() {
        return sx;
    }

    public void setSx(Short sx) {
        this.sx = sx;
    }

    public Byte getBy() {
        return by;
    }

    public void setBy(Byte by) {
        this.by = by;
    }

    public Byte getBx() {
        return bx;
    }

    public void setBx(Byte bx) {
        this.bx = bx;
    }

    public Character getCy() {
        return cy;
    }

    public void setCy(Character cy) {
        this.cy = cy;
    }

    public Character getCx() {
        return cx;
    }

    public void setCx(Character cx) {
        this.cx = cx;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean getBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}