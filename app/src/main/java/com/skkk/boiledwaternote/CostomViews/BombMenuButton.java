package com.skkk.boiledwaternote.CostomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.skkk.boiledwaternote.R;

/**
 * 创建于 2017/6/27
 * 作者 admin
 */
/*
* 
* 描    述：点击弹出的button
* 作    者：ksheng
* 时    间：2017/6/27$ 21:33$.
*/
public class BombMenuButton extends RelativeLayout{

    private int itemCount;
    private int orientation;

    public BombMenuButton(Context context) {
        super(context);
        initView();
    }

    public BombMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView();
    }

    public BombMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta=context.obtainStyledAttributes(attrs,
                R.styleable.ComboBox);

        //获取Item个数
        itemCount = ta.getInt(R.styleable.BombMenuButton_itemCount,0);
        //获取Item显示方向
        orientation = ta.getInt(R.styleable.BombMenuButton_orientation,0);

        //获取完值之后我们需要调用recycle()方法来避免重新创建的时候的错误
        ta.recycle();
    }

    private void initView() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){

        }
    }
}
