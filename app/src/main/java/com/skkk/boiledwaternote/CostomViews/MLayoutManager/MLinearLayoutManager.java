package com.skkk.boiledwaternote.CostomViews.MLayoutManager;

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
public class MLinearLayoutManager extends LinearLayoutManager implements LayoutManagerScrollImpl{
    private boolean isScroll = true;

    public MLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void setScroll(boolean isScroll) {
        this.isScroll=isScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isScroll && super.canScrollVertically();
    }
}