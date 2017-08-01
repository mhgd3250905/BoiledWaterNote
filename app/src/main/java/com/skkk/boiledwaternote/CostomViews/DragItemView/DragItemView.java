package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by admin on 2017/6/5.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/5$ 22:21$.
*/
public class DragItemView extends FrameLayout {
    private String TAG = DragItemView.class.getSimpleName();
    private ViewDragHelper dragHelper;
    private CardView llShow;
    private CardView llHide;
    private int leftborder;
    private int middleBorder;//可以拖拽的最大距离
    private int rightBorder;
    private OnDragPosChangeListener onDragPosChangeListener;
    private boolean isOpen = true;

    private int d = 1;
    private boolean isMenuShow = false;


    public boolean isMenuShow() {
        return isMenuShow;
    }

    public interface OnDragPosChangeListener {
        void isDragListener(DragItemView item, View changedView, int left, int top, int dx, int dy);

        void closeDragListener(DragItemView item, View changedView, int left, int top, int dx, int dy);
    }

    public void setOnDragPosChangeListener(OnDragPosChangeListener onDragPosChangeListener) {
        this.onDragPosChangeListener = onDragPosChangeListener;
    }

    public DragItemView(Context context) {
        super(context);
        mInit();
    }

    public DragItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();


    }

    public DragItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    private void mInit() {
        dragHelper = ViewDragHelper.create(this, callback);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftborder = llShow.getLeft();//获取左边界
        rightBorder = (int) (llShow.getLeft() + llShow.getMeasuredWidth() * 0.5);//终点坐标
        middleBorder = (leftborder + rightBorder) / 2;//获取最大滑动距离
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llHide = (CardView) getChildAt(0);
        llShow = (CardView) getChildAt(1);
        //手动设置上层View具有10dp的阴影，下层View没有阴影
        ((CardView) getChildAt(0)).setCardElevation(0);
        ((CardView) getChildAt(1)).setCardElevation(5);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        private boolean isRelease;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == llShow;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        /**
         * 位置发生变化
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.i(TAG, "onViewPositionChanged: 位置变化left: " + left);
            if (left <= leftborder) {
                //如果已经关闭，那么就设状态isOpen false
                Log.i(TAG, "onViewPositionChanged: isOpen-->" + isOpen);
                isOpen = false;
            } else if (left >= rightBorder) {
                //如果全部打开 那么就设置状态为isOpen
                Log.i(TAG, "onViewPositionChanged: isOpen-->" + isOpen);
                isOpen = true;
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            Log.i(TAG, "onViewCaptured: ");
            isRelease = false;
        }

        /**
         * 横向许可
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left <= 0) {
                return 0;
            } else if (left >= rightBorder) {
                return rightBorder;
            }
            return left;
        }

        /**
         * 当ACTION_UP完成
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.i(TAG, "onViewReleased: 结束拖动");
            int smooth = 0;
            if (releasedChild.getLeft() > middleBorder) {
                if (isOpen) {
                    smooth = leftborder;
                } else {
                    smooth = rightBorder;
                }
            } else if (releasedChild.getLeft() < middleBorder) {
                smooth = leftborder;
            }
            dragHelper.smoothSlideViewTo(releasedChild, smooth, releasedChild.getTop());
            ViewCompat.postInvalidateOnAnimation(DragItemView.this);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }
    };

    public void resetItem() {
        if (isMenuShow) {
            bringChildToFront(llHide);
            ((CardView) getChildAt(0)).setCardElevation(0);
            ((CardView) getChildAt(1)).setCardElevation(5);
            llHide = (CardView) getChildAt(0);
            llShow = (CardView) getChildAt(1);
            isMenuShow = !isMenuShow;
        }
    }

    public void resetItemAnim() {
        if (isMenuShow) {
            dragHelper.smoothSlideViewTo(llShow, rightBorder, llShow.getTop());
            ViewCompat.postInvalidateOnAnimation(DragItemView.this);
        }
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
}