package com.ngandroid.demo.ui.pages.ngmodel;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngandroid.demo.R;
import com.ngandroid.demo.model.NgMod;
import com.ngandroid.lib.NgOptions;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

import ng.layout.ActivityNgModelController;

@NgScope(name="ModelFragment")
public class NgModelFragment extends Fragment {
    @NgModel
    NgMod mod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ng_model, container, false);
        new ActivityNgModelController(new NgOptions.Builder().build(), this).attach(v);
        return v;
    }
}

