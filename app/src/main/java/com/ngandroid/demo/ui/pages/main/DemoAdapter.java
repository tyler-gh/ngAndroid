package com.ngandroid.demo.ui.pages.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ngandroid.demo.R;
import com.ngandroid.demo.model.DemoItem;
import com.ngandroid.demo.scope.DemoScope;
import com.ngandroid.lib.NgAndroid;

import java.util.List;

/**
 * Created by tyler on 4/23/15.
 */
class DemoAdapter extends BaseAdapter {

    private final NgAndroid ngAndroid = NgAndroid.getInstance();
    private final LayoutInflater inflater;
    private final DemoScope.FragmentSelectedListener fragmentSelectedListener;
    private final List<DemoItem> items;

    DemoAdapter(Activity activity, DemoScope.FragmentSelectedListener fragmentSelectedListener, List<DemoItem> items) {
        this.inflater = activity.getLayoutInflater();
        this.fragmentSelectedListener = fragmentSelectedListener;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            DemoScope demoScope = new DemoScope(fragmentSelectedListener);
            convertView = ngAndroid.inflate(demoScope, inflater, R.layout.view_demo_item, parent, false);
            convertView.setTag(demoScope);
        }
        ((DemoScope)convertView.getTag()).setItem(items.get(position));
        return convertView;
    }
}
