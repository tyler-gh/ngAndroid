package com.ngandroid.demo.ui.pages.main;

import android.app.Fragment;

/**
 * Created by tyler on 4/23/15.
 */
class DemoItem {
    private String text;
    private Class<? extends Fragment> fragment;

    DemoItem(){}
    DemoItem(String text, Class<? extends Fragment> fragment){
        this.text = text;
        this.fragment = fragment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }
}
