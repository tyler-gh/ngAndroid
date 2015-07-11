package com.ngandroid.demo.scope;

import android.app.Fragment;

import com.ngandroid.demo.model.DemoItem;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

/**
 * Created by tyler on 4/23/15.
 */
@NgScope
public class DemoScope {
    @NgModel
    DemoItem item;

    private final FragmentSelectedListener fragmentSelectedListener;

    public DemoScope(FragmentSelectedListener fragmentSelectedListener){
        this.fragmentSelectedListener = fragmentSelectedListener;
    }

    public void setItem(DemoItem item) {
        this.item.setText(item.getText());
        this.item.setFragment(item.getFragment());
    }

    public void showFragment(Class<? extends Fragment> fragment){
        fragmentSelectedListener.onFragmentSelected(fragment);
    }

    public interface FragmentSelectedListener {
        void onFragmentSelected(Class<? extends Fragment> fragment);
    }

}
