package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

/**
 * Created by tyler on 4/23/15.
 */
@NgScope
class DemoScope {
    @NgModel
    DemoItem item;

    private final Context context;
    DemoScope(Context context){this.context = context;}

    void setItem(DemoItem item) {
        this.item.setText(item.getText());
        this.item.setActivity(item.getActivity());
    }

    void openActivity(Class<? extends Activity> activity){
        context.startActivity(new Intent(context, activity));
    }
}
