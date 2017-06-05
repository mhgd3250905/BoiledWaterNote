package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2017/6/5.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/5$ 22:21$.
*/
public class DragItemView extends ViewGroup {
    private String TAG = DragItemView.class.getSimpleName();
    private ViewDragHelper dragHelper;
    private CardView llShow;
    private CardView llHide;
    private int maxWidth;//可以拖拽的最大距离
    private int leftBorder;
    private boolean dragToRight;//是否向右拖动
    private boolean mIsMoving;//是否正在拖动

    private RecyclerView rv;
    private float centerWidth;
    private OnDragPosChangeListener onDragPosChangeListener;

    private boolean isDraging;
    private boolean lastDragStatus;
    private int d=1;
    private boolean isMenuShow=false;

    public boolean isMenuShow() {
        return isMenuShow;
    }

    public interface OnDragPosChangeListener {
        void isDragListener(DragItemView item, View changedView, int left, int top, int dx, int dy);

        void closeDragListener(DragItemView item,View changedView, int left, int top, int dx, int dy);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            getChildAt(0).layout(l, t, r, b);
            getChildAt(1).layout(l, t, r, b);
            maxWidth = llShow.getMeasuredWidth() / 2;
            Log.i(TAG, "onLayout: left--->"+llShow.getLeft()+" , right--->"+llShow.getRight());
            centerWidth = llShow.getLeft() + (llShow.getMeasuredWidth()) / 2;
            Log.i(TAG, "onLayout: conterWidth--->"+centerWidth);
            leftBorder = llShow.getLeft();
            if (getParent().getParent() instanceof RecyclerView) {
                rv = (RecyclerView) getParent().getParent();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llHide = (CardView) getChildAt(0);
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
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //当上层view拖拽的时候，下层View反向运动
            //拖动的时候下层View逐渐变大 到达终点的时候两个View一样大
            //切换之后上层View变为下层View 越来越小

            if (left <= 0) {
                llHide.setTranslationX(0);
                if (d==-1){
                    llHide = (CardView) getChildAt(0);
                    llShow = (CardView) getChildAt(1);
                    llHide.setScaleX(1);
                    llHide.setScaleY(1);
                    llShow.setScaleX(1);
                    llShow.setScaleY(1);
                    isMenuShow=!isMenuShow;
                    d=1;
                    Log.w(TAG, "onViewPositionChanged: 当前是否为菜单在上"+isMenuShow);
                }
                onDragPosChangeListener.closeDragListener(DragItemView.this,changedView, left, top, dx, dy);
            } else if (left > 0 && left <= centerWidth) {
                float scaleHide = (float) (0.75 - d *0.25 * (centerWidth - left) / maxWidth);
                float scaleShow = (float) (0.75 + d *0.25 * (centerWidth - left) / maxWidth);

                float scaleElevation=(centerWidth - left) / maxWidth;

                llHide.setTranslationX(-left);
                llHide.setScaleX(scaleHide);
                llHide.setScaleY(scaleHide);

                llShow.setScaleX(scaleShow);
                llShow.setScaleY(scaleShow);

                Log.i(TAG, "onViewPositionChanged: left---> " + left + " , scaleShow---> " + scaleShow);
                if (left == centerWidth) {
                    d=-1;
                    bringChildToFront(llHide);
                    dragHelper.smoothSlideViewTo(llShow, 0, top);
                }
                onDragPosChangeListener.isDragListener(DragItemView.this,changedView, left, top, dx, dy);
            }
            ViewCompat.postInvalidateOnAnimation(DragItemView.this);
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
            } else if (left >= centerWidth) {
                return (int) (centerWidth);
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
            //1 拉开太少自动关闭
            //2 拉开不少切换item
            if (releasedChild.getLeft() < centerWidth / 3) {
                dragHelper.smoothSlideViewTo(llShow, 0, releasedChild.getTop());
            } else {
                dragHelper.smoothSlideViewTo(llShow, (int) (centerWidth), releasedChild.getTop());
            }
            ViewCompat.postInvalidateOnAnimation(DragItemView.this);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }
    };


    public void resetItem(){
        if (isMenuShow) {
            bringChildToFront(llHide);
            llHide = (CardView) getChildAt(0);
            llShow = (CardView) getChildAt(1);
            isMenuShow=!isMenuShow;
        }
    }

    public void resetItemAnim(){
        if (isMenuShow) {

            dragHelper.smoothSlideViewTo(llShow, (int) centerWidth, llShow.getTop());
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
