package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.ngandroid.demo.R;
import com.ngandroid.demo.ui.pages.ngmodel.NgModelActivity;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ListView listView = (ListView) findViewById(R.id.listView);

        List<DemoItem> items = new ArrayList<>();

        items.add(new DemoItem("ngModel", NgModelActivity.class));
        items.add(new DemoItem("ngClick", DemoActivity.class));
        items.add(new DemoItem("ngLongClick", DemoActivity.class));
        items.add(new DemoItem("ngChange", DemoActivity.class));
        items.add(new DemoItem("ngGone", DemoActivity.class));
        items.add(new DemoItem("ngInvisible", DemoActivity.class));
        items.add(new DemoItem("ngDisabled", DemoActivity.class));
        items.add(new DemoItem("ngFocus", DemoActivity.class));

        listView.setAdapter(new DemoAdapter(this, items));
    }
}

