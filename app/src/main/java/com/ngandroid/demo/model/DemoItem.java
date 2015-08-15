package com.ngandroid.demo.model;

import android.app.Fragment;

public class DemoItem {
    private String text;
    private Class<? extends Fragment> fragment;

    public DemoItem(){}
    public DemoItem(String text, Class<? extends Fragment> fragment){
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
