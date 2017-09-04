package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skkk.boiledwaternote.R;

/**
 * Created by admin on 2017/4/11.
 */

// TODO: 2017/8/13 菜单状态reset功能，在每次滑动复用Item的时候都还原为初始状态
/*
* 
* 描    述：常见的可以拖动删除的Item
* 作    者：ksheng
* 时    间：2017/4/11$ 22:06$.
*/
public class MyDragItemView extends ViewGroup {

    private ViewDragHelper dragHelper;
    private CardView llShow;
    private LinearLayout llHide;
    private int maxWidth;//可以拖拽的最大距离
    private int leftBorder;
    private boolean dragToRight;//是否向右拖动
    private boolean isDraging;//是否正在拖动
    private OnItemDragStatusChange onItemDragStatusChange;//菜单拖拽状态打开切换监听
    private OnDragItemClickListener onDragItemClickListener;//拖拽Item点击事件
    private int l,t,r,b;
    private boolean isMenuOpen;


    public interface OnDragItemClickListener{
        void onItemClickListener(View view);
        void onItemMenuCloseClickListener(View view);
    }

    public interface OnItemDragStatusChange{
        void onItemDragStatusOpen();
        void onItemDragStatusClose();
        void onItemMenuStatusOpen();
        void onItemMenuStatusClose();
    }

    public void setOnItemDragStatusChange(OnItemDragStatusChange onItemDragStatusChange) {
        this.onItemDragStatusChange = onItemDragStatusChange;
    }

    public void setOnDragItemClickListener(final OnDragItemClickListener onDragItemClickListener) {
        this.onDragItemClickListener = onDragItemClickListener;
        llShow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen()){
                    onDragItemClickListener.onItemMenuCloseClickListener(v);
                }else {
                    onDragItemClickListener.onItemClickListener(v);
                }
            }
        });
    }

    public MyDragItemView(Context context) {
        super(context);
        mInit();
    }

    public MyDragItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyDragItemView);

    }

    public MyDragItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    private void mInit() {
        dragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            this.l=l;
            this.t=t;
            this.r=r;
            this.b=b;
            getChildAt(0).layout(l, t, r, b);
            getChildAt(1).layout(l, t, r, b);
            maxWidth = llShow.getMeasuredWidth() / 2;
            leftBorder = llShow.getLeft();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llHide = (LinearLayout) getChildAt(0);
        llShow = (CardView) getChildAt(1);
    }



    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == llShow;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left >= leftBorder && left < (leftBorder + maxWidth)) {
                return left;
            } else if (left >= (leftBorder + maxWidth)) {
                return leftBorder + maxWidth;
            }
            return 0;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            //设置滑动状态状态为True
            isDraging = true;
            //设置开启拖拽监听
            onItemDragStatusChange.onItemDragStatusOpen();
            if (capturedChild.getLeft()<=leftBorder){
                setMenuOpen(true);
                onItemDragStatusChange.onItemMenuStatusOpen();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild.getLeft() < (leftBorder + maxWidth / 4 )) {
                closeItem();
            } else if (releasedChild.getLeft() >= (leftBorder + maxWidth / 4)
                    && releasedChild.getLeft() <= (leftBorder + maxWidth)) {
                if (dragToRight) {
                    dragHelper.smoothSlideViewTo(llShow, leftBorder + maxWidth, 0);
                } else {
                    closeItem();
                }
            }
            ViewCompat.postInvalidateOnAnimation(MyDragItemView.this);

            onItemDragStatusChange.onItemDragStatusClose();
            isDraging = false;

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (dx > 0) {
                dragToRight = true;
            } else if (dx < 0) {
                dragToRight = false;
            }
            /**
             * 判断并设置Item的拖拽状态
             */
            if (left<=leftBorder){
                onItemDragStatusChange.onItemMenuStatusClose();
                setMenuOpen(false);
            }

//            if (mIsMoving && rv != null) {
//                rv.setLayoutFrozen(true);
//            } else if(!mIsMoving && rv != null){
//                rv.setLayoutFrozen(false);
//            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

    };

    /**
     * 关闭Item
     */
    public void closeItem() {
        dragHelper.smoothSlideViewTo(llShow, leftBorder, 0);
    }

    public void resetItem(){
        llHide.layout(l,t,r,b);
        llShow.layout(l,t,r,b);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public void setMenuOpen(boolean menuOpen) {
        isMenuOpen = menuOpen;
    }
}
