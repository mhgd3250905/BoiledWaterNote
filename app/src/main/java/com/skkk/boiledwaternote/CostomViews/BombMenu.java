package com.skkk.boiledwaternote.CostomViews;

/**
 * 创建于 2017/6/28
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/28$ 21:58$.
*/

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

/**
 * 创建于 2017/6/28
 * 作者 admin
 */
/*
*
* 描    述：弹射菜单
* 作    者：ksheng
* 时    间：2017/6/28$ 20:50$.
*/
public class BombMenu  extends ViewGroup implements View.OnClickListener {

    private final String TAG=BombMenu.class.getSimpleName();
    private final int POS_LEFT_TOP=0;
    private final int POS_LEFT_BUTTOM=1;
    private final int POS_RIGHT_TOP=2;
    private final int POS_RIGHT_BUTTOM=3;

    private final int STATUS_CLOSE=0;
    private final int STATUS_OPEN=1;

    private final int DIRECTION_HORIZONTAL=0;
    private final int DIRECTION_VERTICAL=1;

    private int mPosition=POS_RIGHT_TOP;
    private int mStatus=STATUS_CLOSE;
    private int mDirection=DIRECTION_VERTICAL;

    private View mainButton;

    private OnMenuItemClickListener mMenuItemClickListener;
    private OnMenuItemTouchListener mMenuItemTouchListener;
    private int width;
    private int height;
    private long animDuration=300;

    public void setmMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public void setmMenuItemTouchListener(OnMenuItemTouchListener mMenuItemTouchListener) {
        this.mMenuItemTouchListener = mMenuItemTouchListener;
    }

    public BombMenu(Context context) {
        super(context);
    }

    public BombMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BombMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    /**
     * 注册自定义属性
     */
    private void initAttrs() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count=getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            layoutButton();
        }
    }

    /**
     * 布局按钮
     */
    private void layoutButton() {
        /*
        * 计算所有按钮的位置
        * */
        int count=getChildCount();
        mainButton=getChildAt(count-1);

        int l=0;
        int t=0;
        width = mainButton.getMeasuredWidth();
        height = mainButton.getMeasuredHeight();

        switch (mPosition){
            case POS_LEFT_TOP:
                break;
            case POS_LEFT_BUTTOM:
                t=getMeasuredHeight()- height;
                break;
            case POS_RIGHT_TOP:
                l=getMeasuredWidth()- width;
                break;
            case POS_RIGHT_BUTTOM:
                l=getMeasuredWidth()- width;
                t=getMeasuredHeight()- height;
                break;
        }
        /*
        * 放置菜单按钮
        * */
        mainButton.layout(l,t,l+ width,t+ height);
        mainButton.setClickable(true);
        mainButton.setFocusable(true);
        mainButton.setOnClickListener(this);
        /*
        * 放置子菜单
        * */
        for (int i = 0; i < count-1; i++) {
            View childView=getChildAt(i);
            childView.layout(l,t,l+ width,t+ height);
            childView.setVisibility(GONE);
            final int pos=i;
            if (i==count-2){        //给最后一个Item设置为触摸监听
                childView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN){
                            if (mMenuItemTouchListener!=null){
                                mMenuItemTouchListener.onItemTouchListener(pos,v);
                            }
                        }
                        return false;
                    }
                });
            }else {
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickItemAnim(pos);
                        startItemClickAnim(v);
                        if (mMenuItemClickListener != null) {
                            mMenuItemClickListener.onItemClickListener(pos, v);
                        }
                    }
                });
            }
        }
    }

    /**
     * 点击Item的动画
     * @param v
     */
    private void startItemClickAnim(View v) {
        ObjectAnimator scaleXAnim=ObjectAnimator.ofFloat(v,"scaleX",1f,1.5f);
        ObjectAnimator scaleYAnim=ObjectAnimator.ofFloat(v,"scaleY",1f,1.5f);
        ObjectAnimator alphaAnim=ObjectAnimator.ofFloat(v,"Alpha",1f,0f);
        AnimatorSet animSet=new AnimatorSet();
        animSet.play(scaleXAnim).with(scaleYAnim).with(alphaAnim);
        animSet.setDuration(200);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        final View animView=v;
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toggleMenu();
                ObjectAnimator scaleXAnimBack=ObjectAnimator.ofFloat(animView,"scaleX",1.5f,1f);
                ObjectAnimator scaleYAnimBack=ObjectAnimator.ofFloat(animView,"scaleY",1.5f,1f);
                ObjectAnimator alphaAnimBack=ObjectAnimator.ofFloat(animView,"Alpha",0f,1f);
                AnimatorSet animSetBack=new AnimatorSet();
                animSetBack.play(scaleXAnimBack).with(scaleYAnimBack).with(alphaAnimBack);
                animSetBack.start();
            }
        });
        animSet.start();
    }


    /**
     * Item点击动画
     * @param pos
     */
    private void clickItemAnim(int pos) {
    }

    @Override
    public void onClick(View v) {
        openMainButton();
        toggleMenu();
    }

    /**
     * 展开菜单
     */
    private void toggleMenu() {
        int itemCount=getChildCount()-1;
        for (int i = 0; i < itemCount; i++) {
            final View child=getChildAt(i);
            child.setVisibility(VISIBLE);
            child.setClickable(true);
            child.setFocusable(true);

            /*
            * 计算出子按钮位移的距离
            * */
            int x=0;
            int y=0;
            if (mDirection==DIRECTION_HORIZONTAL){  //水平平移
                x=(i+1)*width;
            }else if (mDirection==DIRECTION_VERTICAL){  //垂直平移
                y=(i+1)*height;
            }
            /*
            * 判断位移方向
            * */
            int xFlag=1;
            int yFlag=1;
            if (mPosition==POS_RIGHT_TOP||mPosition==POS_RIGHT_BUTTOM){
                xFlag=-1;
            }
            if (mPosition==POS_LEFT_BUTTOM||mPosition==POS_RIGHT_BUTTOM) {
                xFlag=-1;
            }

            ObjectAnimator transXAnimator=null;
            ObjectAnimator transYAnimator=null;
            AnimatorSet animatorSet = new AnimatorSet();
            if (mStatus==STATUS_CLOSE){     //设置弹出动画

                animatorSet.setInterpolator(new BounceInterpolator());
                animatorSet.setDuration(animDuration);

                transXAnimator=ObjectAnimator.ofFloat(child,"translationX",
                        0,x*xFlag);
                transYAnimator=ObjectAnimator.ofFloat(child,"translationY",
                        0,y*yFlag);
            }else {     //设置回收动画
                animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSet.setDuration(animDuration*2/3);

                transXAnimator=ObjectAnimator.ofFloat(child,"translationX",
                        x*xFlag,0);
                transYAnimator=ObjectAnimator.ofFloat(child,"translationY",
                        y*yFlag,0);
            }
            animatorSet.play(transXAnimator).with(transYAnimator);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mStatus==STATUS_CLOSE){
                        child.setVisibility(GONE);
                        child.setClickable(false);
                        child.setFocusable(false);
                    }
                    super.onAnimationEnd(animation);
                }
            });
            animatorSet.start();
        }
        changeStatus();
    }

    /**
     * 切换状态
     */
    private void changeStatus() {
        mStatus=(mStatus==STATUS_CLOSE?STATUS_OPEN:STATUS_CLOSE);
    }

    /**
     * 打开主菜单
     */
    private void openMainButton() {

    }
}

