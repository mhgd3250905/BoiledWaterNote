package com.skkk.boiledwaternote.CostomViews.RecyclerEditView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.DensityUtil;


/**
 * Created by admin on 2017/4/22.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/22$ 23:31$.
*/
public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback{
    private Context context;
    private ItemTouchHelperAdapter itemTouchHelperAdapter;

    public MyItemTouchHelperCallback(Context context,ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.context=context;
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag= ItemTouchHelper.UP| ItemTouchHelper.DOWN;
        int swipeFlag= ItemTouchHelper.START| ItemTouchHelper.END;
        return makeMovementFlags(dragFlag,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //拖拽移动的时候处理
        if (itemTouchHelperAdapter !=null){
            itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.1f;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //左右滑动的时候处理
        if (itemTouchHelperAdapter !=null){
            itemTouchHelperAdapter.onitemSwipe(viewHolder.getAdapterPosition());
        }
    }

    /**
     * 在不同的状态进行不同状态的绘制
     * @param c 画板
     * @param recyclerView rv
     * @param viewHolder viewHolder
     * @param dX x方向拖动
     * @param dY y方向拖动
     * @param actionState 基本拖动类型：
     *                    ACTION_STATE_DRAG-->拖拽
     *                    ACTION_STATE_SWIPE-->滑动
     *                    ACTION_STATE_IDLE-->闲置
     * @param isCurrentlyActive 当前状态
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View item = viewHolder.itemView;
        ViewGroup.LayoutParams layoutParams = item.getLayoutParams();
        //如果当前是拖拽状态
        if (actionState== ItemTouchHelper.ACTION_STATE_DRAG){
            if (isCurrentlyActive){//正在拖动
                if (layoutParams.height> DensityUtil.dip2px(context,context.getResources().getDimension(R.dimen.edit_item_image_min_height))){
                    layoutParams.height-=50;
                }else {
                    layoutParams.height=DensityUtil.dip2px(context,context.getResources().getDimension(R.dimen.edit_item_image_min_height));
                }
            }else {
                if (layoutParams.height<DensityUtil.dip2px(context,context.getResources().getDimension(R.dimen.edit_item_image_max_height))){
                    layoutParams.height+=50;
                }else {
                    layoutParams.height=DensityUtil.dip2px(context,context.getResources().getDimension(R.dimen.edit_item_image_max_height));
                }
            }
            item.setLayoutParams(layoutParams);
        }
    }

    /**
     * 是否可以长按触发拖拽
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 是否可以侧滑
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
}
