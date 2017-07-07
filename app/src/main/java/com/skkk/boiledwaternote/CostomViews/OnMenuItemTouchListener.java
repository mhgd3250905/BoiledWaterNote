package com.skkk.boiledwaternote.CostomViews;

import android.view.View;

/**
 * 创建于 2017/6/28
 * 作者 admin
 */

public interface OnMenuItemTouchListener {
    void onItemTouchListener(int pos, View v);
    void onItemTouchLeaveListener(int pos, View v);
}
