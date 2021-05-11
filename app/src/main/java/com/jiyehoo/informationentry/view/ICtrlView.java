package com.jiyehoo.informationentry.view;

import android.view.View;

public interface ICtrlView {
    void showToast(String msg);
    void addView(View view);
    void finishActivity();
    void showSwipeRefresh(boolean haveShow);
    void clearList();
}
