package com.skkk.boiledwaternote.CostomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 创建于 2017/8/31
 * 作者 admin
 */
/*
* 
* 描    述：只能垂直滑动的RecyclerView
* 作    者：ksheng
* 时    间：2017/8/31$ 23:05$.
*/
public class VerticalRecyclerView extends RecyclerView {
    private float startX;
    private float startY;
    private double touchRate=0.5;

    public interface VerticalTouchListener {
        void onVerticalTouch(MotionEvent e);
    }

    public VerticalRecyclerView(Context context) {
        super(context);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = e.getX();
                startY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Y/X大于0.5-->判断为方向向下 拦截
                // Y/X小于0.5-->分发到子控件
                return Math.abs((e.getY() - startY) / (e.getX() - startX)) >= touchRate;
        }
        return super.onInterceptTouchEvent(e);
    }
}
