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

package com.ngandroid.demo.model.test;

/**
* Created by tyler on 3/23/15.
*/
public interface TestJsonModel {
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
