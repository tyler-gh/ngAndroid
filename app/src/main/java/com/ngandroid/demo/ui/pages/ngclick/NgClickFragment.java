package com.ngandroid.demo.ui.pages.ngclick;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ngandroid.demo.R;
import com.ngandroid.demo.scope.LoginScope;
import com.ngandroid.lib.NgOptions;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

import ng.layout.LoginController;

/**
 * A simple {@link Fragment} subclass.
 */
@NgScope(name="ClickFragment")
public class NgClickFragment extends Fragment {

    public NgClickFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_ng_click, container, false);
//        new FragmentNgClickController(new NgOptions.Builder().build(), this).attach(v);

        View v = inflater.inflate(R.layout.login, container, false);
        new LoginController(new NgOptions.Builder().build(), new LoginScope()).attach(v);

        return v;
    }

    @NgModel
    Inv inv;

    public static class Inv {
        private boolean invisible, gone, disabled, focus, focus1;

        public boolean getInvisible() {
            return invisible;
        }

        public void setInvisible(boolean invisible) {
            this.invisible = invisible;
        }

        public boolean getGone() {
            return gone;
        }

        public void setGone(boolean gone) {
            this.gone = gone;
        }

        public boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public boolean getFocus() {
            return focus;
        }

        public void setFocus(boolean focus) {
            this.focus = focus;
        }

        public boolean getFocus1() {
            return focus1;
        }

        public void setFocus1(boolean focus1) {
            this.focus1 = focus1;
        }
    }

    void makeToast(String value){
        Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
    }
}
