package com.ngandroid.demo.ui.pages.ngclick;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ngandroid.lib.annotations.NgScope;

/**
 * A simple {@link Fragment} subclass.
 */
@NgScope(name="ClickFragment")
public class NgClickFragment extends Fragment {

//    NgAndroid ng = NgAndroid.getInstance();

    public NgClickFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new View(getActivity());//ng.inflate(this, inflater, R.layout.fragment_ng_click, container, false);
    }

    void makeToast(){
        Toast.makeText(getActivity(), "Toast Made!", Toast.LENGTH_SHORT).show();
    }
}
