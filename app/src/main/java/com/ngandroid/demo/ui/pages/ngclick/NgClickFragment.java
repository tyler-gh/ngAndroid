package com.ngandroid.demo.ui.pages.ngclick;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ngandroid.demo.R;
import com.ngandroid.lib.NgOptions;
import com.ngandroid.lib.annotations.NgScope;

import ng.layout.NgFragmentNgClick;

/**
 * A simple {@link Fragment} subclass.
 */
@NgScope(name="ClickFragment")
public class NgClickFragment extends Fragment {

    public NgClickFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ng_click, container, false);
        new NgFragmentNgClick(new NgOptions.Builder().build(), this).attach(v);
        return v;
    }

    void makeToast(){
        Toast.makeText(getActivity(), "Toast Made!", Toast.LENGTH_SHORT).show();
    }
}
