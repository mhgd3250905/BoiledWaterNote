package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by admin on 2017/6/5.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/5$ 22:19$.
*/
public class MyLinearLayoutManager extends LinearLayoutManager {
    private boolean isScroll = true;

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isScroll && super.canScrollVertically();
    }
}