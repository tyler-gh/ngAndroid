package com.ngandroid.demo.ui.pages.main;

import android.app.Fragment;

import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

/**
 * Created by tyler on 4/23/15.
 */
@NgScope
class DemoScope {
    @NgModel
    DemoItem item;

    private final FragmentSelectedListener fragmentSelectedListener;

    DemoScope(FragmentSelectedListener fragmentSelectedListener){
        this.fragmentSelectedListener = fragmentSelectedListener;
    }

    void setItem(DemoItem item) {
        this.item.setText(item.getText());
        this.item.setFragment(item.getFragment());
    }

    void showFragment(Class<? extends Fragment> fragment){
        fragmentSelectedListener.onFragmentSelected(fragment);
    }

    public interface FragmentSelectedListener {
        void onFragmentSelected(Class<? extends Fragment> fragment);
    }

}
