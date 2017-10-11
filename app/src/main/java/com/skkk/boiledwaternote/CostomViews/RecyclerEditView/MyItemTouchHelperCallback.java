package com.skkk.boiledwaternote.CostomViews.RecyclerEditView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.skkk.boiledwaternote.CostomViews.RichEdit.NoteEditAdapter;


/**
 * Created by admin on 2017/4/22.
 */

//TODO:解决ItemTouchHelper的Move识别问题
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/22$ 23:31$.
*/
public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private Context context;
    private ItemTouchHelperAdapter itemTouchHelperAdapter;
    private NoteEditAdapter.NoteEditViewHolder lastHolder;
    private boolean isUp;//垂直方向

    public MyItemTouchHelperCallback(Context context, ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.context = context;
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag, 0);
    }

    /**
     * 开始移动
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //拖拽移动的时候处理
        if (lastHolder == null) {
            lastHolder = (NoteEditAdapter.NoteEditViewHolder) target;
        } else if (lastHolder.getAdapterPosition() != target.getAdapterPosition()) {
            lastHolder.ivSwipeNotice.setVisibility(View.GONE);
            lastHolder = (NoteEditAdapter.NoteEditViewHolder) target;
        }

        lastHolder.ivSwipeNotice.setVisibility(View.VISIBLE);

        if (itemTouchHelperAdapter != null) {
            itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    /**
     * 开始移动返回true
     *
     * @param recyclerView
     * @param viewHolder
     * @param fromPos
     * @param target
     * @param toPos
     * @param x
     * @param y
     */
    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        isUp = (y <= 0);
    }


    /**
     * 结束拖拽
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (lastHolder != null) {
            lastHolder.ivSwipeNotice.setVisibility(View.GONE);
        }
        NoteEditAdapter.NoteEditViewHolder holder = (NoteEditAdapter.NoteEditViewHolder) viewHolder;
        holder.ivNoteImageChecked.setVisibility(View.GONE);
        itemTouchHelperAdapter.onItemMoveDone();
    }

    /**
     * 获取拖拽识别范围
     *
     * @param viewHolder
     * @return
     */
    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0f;
    }

    /**
     * 左右拖拽的时候发生
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //左右滑动的时候处理
        if (itemTouchHelperAdapter != null) {
            itemTouchHelperAdapter.onitemSwipe(viewHolder.getAdapterPosition());
        }
    }

    /**
     * 在不同的状态进行不同状态的绘制
     *
     * @param c                 画板
     * @param recyclerView      rv
     * @param viewHolder        viewHolder
     * @param dX                x方向拖动
     * @param dY                y方向拖动
     * @param actionState       基本拖动类型：
     *                          ACTION_STATE_DRAG-->拖拽
     *                          ACTION_STATE_SWIPE-->滑动
     *                          ACTION_STATE_IDLE-->闲置
     * @param isCurrentlyActive 当前状态
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View item = viewHolder.itemView;
    }


    /**
     * 是否可以长按触发拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * 是否可以侧滑
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
}
