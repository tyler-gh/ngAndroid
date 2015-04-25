package com.ngandroid.demo.ui.pages.ngmodel;

import android.app.Activity;
import android.os.Bundle;

import com.ngandroid.demo.R;
import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;
@NgScope
public class NgModelActivity extends Activity {
    private final NgAndroid ng = NgAndroid.getInstance();

    @NgModel
    NgMod mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ng.setContentView(this, R.layout.activity_ng_model);
    }
}

class NgMod {
    private String str;
    private int integer;
    private double dub;
    private float flt;
    private boolean bool;
    private long lon;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public double getDub() {
        return dub;
    }

    public void setDub(double dub) {
        this.dub = dub;
    }

    public float getFlt() {
        return flt;
    }

    public void setFlt(float flt) {
        this.flt = flt;
    }

    public boolean getBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }
}