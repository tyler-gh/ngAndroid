package com.ngandroid.demo.ui.pages.ngmodel;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.demo.R;
import com.ngandroid.demo.model.NgMod;
import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;
@NgScope
public class NgModelFragment extends Fragment {
    private final NgAndroid ng = NgAndroid.getInstance();

    @NgModel
    NgMod mod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return ng.inflate(this, inflater, R.layout.activity_ng_model, container, false);
    }
}

