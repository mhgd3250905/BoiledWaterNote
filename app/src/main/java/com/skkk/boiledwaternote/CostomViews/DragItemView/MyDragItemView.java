package com.skkk.boiledwaternote.CostomViews.DragItemView;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.content.ContentValues.TAG;

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
    private int position;
    private float sensitivity=2f;//拖拽灵敏度


    public interface OnDragItemClickListener{
        void onItemClickListener(View view);
        void onItemMenuCloseClickListener(View view);
    }

    public interface OnItemDragStatusChange{
        void onItemDragStatusOpen(int pos);
        void onItemDragStatusClose(int pos);
        void onItemMenuStatusOpen(int pos);
        void onItemMenuStatusClose(int pos);
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
    }

    public MyDragItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    private void mInit() {
        dragHelper = ViewDragHelper.create(this,sensitivity, callback);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        ViewGroup.LayoutParams cParams = null;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (ViewGroup.LayoutParams) childView.getLayoutParams();

            // 上面两个childView
            if (i == 0 || i == 1)
            {
                tWidth += cWidth ;
            }

            if (i == 2 || i == 3)
            {
                bWidth += cWidth ;
            }

            if (i == 0 || i == 2)
            {
                lHeight += cHeight ;
            }

            if (i == 1 || i == 3)
            {
                rHeight += cHeight;
            }

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
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
            maxWidth = llShow.getMeasuredWidth()*2/ 3;
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
            Log.i(TAG, "onViewCaptured: ");
            //设置滑动状态状态为True
            isDraging = true;
            //设置开启拖拽监听
            if (onItemDragStatusChange!=null) {
                onItemDragStatusChange.onItemDragStatusOpen(getPosition());
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
//            onItemDragStatusChange.onItemDragStatusClose();
            isDraging = false;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (dx > 0) {
                dragToRight = true;
                if (onItemDragStatusChange!=null) {
                    onItemDragStatusChange.onItemDragStatusOpen(getPosition());
                }
            } else if (dx < 0) {
                dragToRight = false;
                if (onItemDragStatusChange!=null) {
                    onItemDragStatusChange.onItemDragStatusClose(getPosition());
                }
            }
            /**
             * 判断并设置Item的拖拽状态
             */
            if (left<=leftBorder){
                if (onItemDragStatusChange!=null) {
                    onItemDragStatusChange.onItemMenuStatusClose(getPosition());
                }
                setMenuOpen(false);
            }
            if (left>=leftBorder+maxWidth){
                setMenuOpen(true);
                if (onItemDragStatusChange!=null) {
                    onItemDragStatusChange.onItemMenuStatusOpen(getPosition());
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

    };

    /**
     * 打开Item
     */
    public void openItem(){
        if (llShow.getLeft()<=leftBorder) {
            dragHelper.smoothSlideViewTo(llShow, leftBorder + maxWidth, 0);
            ViewCompat.postInvalidateOnAnimation(MyDragItemView.this);
        }
    }

    /**
     * 关闭Item
     */
    public void closeItem() {
        if (llShow.getLeft()>leftBorder){
            dragHelper.smoothSlideViewTo(llShow, leftBorder, 0);
            ViewCompat.postInvalidateOnAnimation(MyDragItemView.this);
        }
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
