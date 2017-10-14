package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by admin on 2017/6/5.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/5$ 22:19$.
*/
public class MStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScroll = true;

    public MStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isScroll && super.canScrollVertically();
    }
}