package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.ngandroid.demo.R;
import com.ngandroid.demo.ui.pages.ngmodel.NgModelActivity;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends Activity {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setTitle(R.string.app_name);

        drawerToggle = new ToolbarDrawerToggle(drawerLayout, toolbar);
        drawerLayout.setDrawerListener(drawerToggle);

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private final class ToolbarDrawerToggle extends ActionBarDrawerToggle {
        public ToolbarDrawerToggle(DrawerLayout drawerLayout, Toolbar toolbar) {
            super(DemoActivity.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        public void onDrawerClosed(View view){
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        public void onDrawerOpened(View drawerView){
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
    }
}

