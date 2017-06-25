package com.skkk.boiledwaternote.CostomViews.RecyclerEditView;

/**
 * Created by admin on 2017/4/22.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/22$ 23:34$.
*/
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPos, int toPos);    //正在更换条目位置
    void onitemSwipe(int pos);    //正在滑动删除
    void onItemMoveDone();    //完成更换条目位置
}
