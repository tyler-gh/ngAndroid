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

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Created by tyler on 2/16/15.
 */
public class BlurUtils {
    private static final float SCALE_RATIO = 5f;
    private static final float DEFAULT_BLUR_RADIUS = 5.f;

    public static Bitmap blurBitmap(Bitmap bitmap, Context context){
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        Bitmap b = Bitmap.createScaledBitmap(Bitmap.createScaledBitmap(bitmap,(int)(width/SCALE_RATIO), (int)(height/SCALE_RATIO), false), width, height, false);
        return blurBitmap(b, DEFAULT_BLUR_RADIUS, context);
    }


    private static Bitmap blurBitmap(Bitmap src, float blurRadius, Context context){
        RenderScript rs = RenderScript.create(context);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap blurredBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), conf);

        final Allocation input = Allocation.createFromBitmap(rs, src, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());

        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(blurRadius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurredBitmap);
        return blurredBitmap;
    }
}
