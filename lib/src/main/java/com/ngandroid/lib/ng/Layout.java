package com.ngandroid.lib.ng;

import android.view.View;

public interface Layout {
    int getId();
    Object execute(int viewId, int attr);
    void attach(View __view__);
}
