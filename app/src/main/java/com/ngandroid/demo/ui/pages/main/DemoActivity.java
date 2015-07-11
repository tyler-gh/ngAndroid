package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ngandroid.demo.R;
import com.ngandroid.demo.model.DemoItem;
import com.ngandroid.demo.scope.DemoScope;
import com.ngandroid.demo.ui.pages.ngclick.NgClickFragment;
import com.ngandroid.demo.ui.pages.ngmodel.NgModelFragment;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends Activity implements DemoScope.FragmentSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        fragmentManager = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setTitle(R.string.app_name);

        drawerToggle = new ToolbarDrawerToggle(drawerLayout, toolbar);
        drawerLayout.setDrawerListener(drawerToggle);

        ListView listView = (ListView) findViewById(R.id.listView);

        List<DemoItem> items = new ArrayList<>();

        items.add(new DemoItem("ngModel", NgModelFragment.class));
        items.add(new DemoItem("ngClick", NgClickFragment.class));
        items.add(new DemoItem("ngLongClick", NgModelFragment.class));
        items.add(new DemoItem("ngChange", NgModelFragment.class));
        items.add(new DemoItem("ngGone", NgModelFragment.class));
        items.add(new DemoItem("ngInvisible", NgModelFragment.class));
        items.add(new DemoItem("ngDisabled", NgModelFragment.class));
        items.add(new DemoItem("ngFocus", NgModelFragment.class));

        listView.setAdapter(new DemoAdapter(this, this, items));

        if(savedInstanceState == null){
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, new NgModelFragment(), "frag").commit();
        }
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

    @Override
    public void onFragmentSelected(Class<? extends Fragment> fragment) {
        Fragment current = fragmentManager.findFragmentByTag("frag");
        if(current != null){
            fragmentManager.beginTransaction().remove(current).commit();
        }

        Fragment frag = getFragment(fragment);
        if(frag != null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, frag, "frag").commit();
        }
        drawerLayout.closeDrawers();
    }

    private Fragment getFragment(Class<? extends Fragment> fragClass){
        try {
            return fragClass.newInstance();
        } catch (Exception e) {
            Toast.makeText(this, "Error showing " + fragClass.getSimpleName().replace("Fragment", ""), Toast.LENGTH_SHORT).show();
            Log.e("DemoScope", "Unable to create Fragment", e);
            return null;
        }
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

