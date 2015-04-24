package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;

/**
 * Created by tyler on 4/23/15.
 */
class DemoItem {
    private String text;
    private Class<? extends Activity> activity;

    DemoItem(){}
    DemoItem(String text, Class<? extends Activity> activity){
        this.text = text;
        this.activity = activity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<? extends Activity> activity) {
        this.activity = activity;
    }
}
