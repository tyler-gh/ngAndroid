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

package com.ngandroid.lib.ngattributes.ngblur;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ngandroid.lib.R;
import com.ngandroid.lib.ng.Model;
import com.ngandroid.lib.ng.ModelMethod;
import com.ngandroid.lib.ngattributes.ngif.NgIf;
import com.ngandroid.lib.utils.BlurUtils;

/**
 * Created by tyler on 2/16/15.
 */
public class NgBlur extends NgIf{
    private static NgBlur ourInstance = new NgBlur();

    public static NgBlur getInstance() {
        return ourInstance;
    }

    private NgBlur() {
    }

    @Override
    protected ModelMethod getModelMethod(final Model model, final View view, final String field) {
        ViewGroup parent = (ViewGroup) view.getParent();
        FrameLayout layout = new FrameLayout(view.getContext());
        layout.setLayoutParams(view.getLayoutParams());
        int index = parent.indexOfChild(view);
        parent.removeViewAt(index);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        layout.addView(view);
        parent.addView(layout, index);

        final ImageView imageView = new ImageView(view.getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.setVisibility(View.GONE);
        layout.addView(imageView);

        return new ModelMethod() {
            @Override
            public Object invoke(String fieldName, Object... args) {
                try {
                    if(model.getValue(field)){
                        view.setVisibility(View.GONE);
                        view.setDrawingCacheEnabled(true);
                        imageView.setImageBitmap(BlurUtils.blurBitmap(view.getDrawingCache(), view.getContext()));
                        imageView.setVisibility(View.VISIBLE);
                    }else{
                        view.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public int getAttribute() {
        return R.styleable.ngAndroid_ngBlur;
    }
}
