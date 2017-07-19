package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
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
public class DragItemCircleView extends FrameLayout {
    private String TAG = DragItemCircleView.class.getSimpleName();
    private ViewDragHelper dragHelper;
    private CardView llShow;
    private CardView llHide;
    private float leftborder;
    private int maxWidth;//可以拖拽的最大距离

    private int centerWidth;
    private OnDragPosChangeListener onDragPosChangeListener;

    private int d = 1;
    private boolean isMenuShow = false;



    public boolean isMenuShow() {
        return isMenuShow;
    }

    public interface OnDragPosChangeListener {
        void isDragListener(DragItemCircleView item, View changedView, int left, int top, int dx, int dy);

        void closeDragListener(DragItemCircleView item, View changedView, int left, int top, int dx, int dy);
    }

    public void setOnDragPosChangeListener(OnDragPosChangeListener onDragPosChangeListener) {
        this.onDragPosChangeListener = onDragPosChangeListener;
    }

    public DragItemCircleView(Context context) {
        super(context);
        mInit();
    }

    public DragItemCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();


    }

    public DragItemCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    private void mInit() {
        dragHelper = ViewDragHelper.create(this, callback);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftborder=llShow.getLeft();//获取左边界
        maxWidth = llShow.getMeasuredWidth() / 2;//获取最大滑动距离
        centerWidth = (int) (llShow.getLeft() + llShow.getMeasuredWidth()*0.5);//终点坐标
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
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == llShow;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            //当上层view拖拽的时候，下层View反向运动
            //拖动的时候下层View逐渐变大 到达终点的时候两个View一样大
            //切换之后上层View变为下层View 越来越小

            /*
            * 如果left等于0了，说明就是切换一次结束，各自归位状态
            * flag d的判断：如果是-1，那么就说明已经完成了卡片切换 而且上下层View位置也发生了交换
            * 处理：
            * 1.重新对上下层View设置阴影高度
            * 2.重置llHide llShow对象
            * 3.强制将卡片大小复原
            * 4.修改卡片显示flag
            * 5.重置flag d=1
            * 6.设置交换完毕监听
            * */
            if (left <= 0) {
                llHide.setTranslationX(0);
                if (d == -1) {
                    ((CardView) getChildAt(0)).setCardElevation(0);
                    ((CardView) getChildAt(1)).setCardElevation(5);
                    llHide = (CardView) getChildAt(0);
                    llShow = (CardView) getChildAt(1);
                    llHide.setScaleX(1);
                    llHide.setScaleY(1);
                    llShow.setScaleX(1);
                    llShow.setScaleY(1);
                    isMenuShow = !isMenuShow;
                    d = 1;
//                    Log.w(TAG, "onViewPositionChanged: 当前是否为菜单在上" + isMenuShow);
                }
                onDragPosChangeListener.closeDragListener(DragItemCircleView.this, changedView, left, top, dx, dy);
            } else if (left > 0 && left <= centerWidth) {
                /*
                * left处于(0:center]范围内，说明正在切换中
                * 1.设置上层View阴影为10 下层View阴影为5
                * 2.卡片按照拖动比例进行缩放和移动
                * 3.如果抵达centerWidth位置说明完成卡片交换
                *       3.1 设置卡片交换flag d=-1
                *       3.2 将下层View置顶
                *       3.3 将下层View（此时llShow已经被挪到下层）归为，上层View（llHide）会按比例归为移动
                *       3.4 设置正在交换监听
                * */
                ((CardView) getChildAt(0)).setCardElevation(2);
                ((CardView) getChildAt(1)).setCardElevation(5);
                float scaleHide = (float) (0.8 - d * 0.2 * (centerWidth - left) / maxWidth);
                float scaleShow = (float) (0.8 + d * 0.2 * (centerWidth - left) / maxWidth);
                llHide.setTranslationX(-left);
                llHide.setScaleX(scaleHide) ;
                llHide.setScaleY(scaleHide);
                llShow.setScaleX(scaleShow);
                llShow.setScaleY(scaleShow);
//                Log.i(TAG, "onViewPositionChanged: scaleHide---> " + scaleHide + " , scaleShow---> " + scaleShow);
//                Log.i(TAG, "onViewPositionChanged: left--->"+left+" , centerWidth--->"+centerWidth);
                if (left >= centerWidth) {
                    d = -1;
                    bringChildToFront(llHide);
                    dragHelper.smoothSlideViewTo(llShow, 0, top);
                }
                onDragPosChangeListener.isDragListener(DragItemCircleView.this, changedView, left, top, dx, dy);
            }
            ViewCompat.postInvalidateOnAnimation(DragItemCircleView.this);
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
                dragHelper.smoothSlideViewTo(llShow, centerWidth, releasedChild.getTop());
            }
            ViewCompat.postInvalidateOnAnimation(DragItemCircleView.this);
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
            dragHelper.smoothSlideViewTo(llShow, (int) centerWidth, llShow.getTop());
            ViewCompat.postInvalidateOnAnimation(DragItemCircleView.this);
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