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
import android.view.LayoutInflater;

import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.attacher.AttributeAttacher;
import com.ngandroid.lib.attacher.InflaterFactory;

import java.lang.reflect.Field;

/**
 * Created by tyler on 3/9/15.
 */
public class ViewBindTests  extends ApplicationTestCase<Application> {
    public ViewBindTests() {
        super(Application.class);
    }

    public interface Note {
        public long getTime();
        public void setTime(long time);
        public int getId();
        public void setId(int id);
        public String getTimeString();
        public void setTimeString(String timeString);
        public String getTitle();
        public void setTitle(String title);
        public String getText();
        public void setText(String text);
    }

    public static class ViewScope {
        Note note;
    }

    public void testSettingValuesAfterInflation(){
        ViewScope scope = new ViewScope();
        NgAndroid.getInstance().inflate(scope, LayoutInflater.from(getContext()), R.layout.test_view, null);
        scope.note.setId(100);
        assertEquals(100, scope.note.getId());
    }

    public void testInflaterFactoryNotReCreated() throws NoSuchFieldException, IllegalAccessException {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        AttributeAttacher attributeAttacher = new AttributeAttacher(inflater, null, null);
        Field f = AttributeAttacher.class.getDeclaredField("mInflater");
        f.setAccessible(true);
        LayoutInflater i1 = (LayoutInflater) f.get(attributeAttacher);
        assertEquals(inflater, i1);
        InflaterFactory factory1 = (InflaterFactory) i1.getFactory();
        attributeAttacher = new AttributeAttacher(inflater, null, null);
        LayoutInflater i2 = (LayoutInflater) f.get(attributeAttacher);
        assertEquals(inflater, i2);
        assertEquals(factory1, (InflaterFactory) i2.getFactory());

    }




}
