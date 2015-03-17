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

package com.ngandroid.lib.utils;

import com.ngandroid.lib.ng.ModelBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by tyler on 2/24/15.
 */
public class JsonUtils {

    public static <T> T buildModelFromJson(String json, Class<T> clzz) throws JSONException {
        return buildModelFromJson(new JSONObject(json), clzz);
    }

    public static <T> T buildModelFromJson(JSONObject jsonObject , Class<T> clzz) throws JSONException {

        ModelBuilder builder = new ModelBuilder(clzz);
        Iterator<String> jsonkeys = jsonObject.keys();
        while(jsonkeys.hasNext()){
            String key = jsonkeys.next();
            String keyLower = key.toLowerCase();
            if(builder.hasField(keyLower)){
                int type = builder.getFieldType(keyLower);
                switch(type){
                    case TypeUtils.BOOLEAN:
                        builder.setField(keyLower, type, jsonObject.getBoolean(key));
                        break;
                    case TypeUtils.INTEGER:
                        builder.setField(keyLower, type, jsonObject.getInt(key));
                        break;
                    case TypeUtils.STRING:
                        builder.setField(keyLower, type, jsonObject.getString(key));
                        break;
                    case TypeUtils.DOUBLE:
                        builder.setField(keyLower, type, jsonObject.getDouble(key));
                        break;
                    case TypeUtils.FLOAT:
                        builder.setField(keyLower, type, (float) jsonObject.getDouble(key));
                        break;
                    case TypeUtils.LONG:
                        builder.setField(keyLower, type, jsonObject.getLong(key));
                        break;
                    case TypeUtils.OBJECT: {
                        Class clss = builder.getFieldTypeClass(keyLower);
                        if(clss.isInterface()){
                            builder.setField(keyLower, type, buildModelFromJson(jsonObject.getJSONObject(key), clss));
                        }
                        break;
                    }
                }

            }
        }
        return (T) builder.create();
    }


    public static String modelToJson(Object obj){
        JSONObject jsonObject = new JSONObject();



        return jsonObject.toString();
    }


}
