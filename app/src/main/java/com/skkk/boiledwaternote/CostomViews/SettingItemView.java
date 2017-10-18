package com.skkk.boiledwaternote.CostomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skkk.boiledwaternote.R;

/**
 * 创建于 2017/10/18
 * 作者 admin
 */
/*
* 
* 描    述：设置栏位Item
* 作    者：ksheng
* 时    间：2017/10/18$ 22:36$.
*/
public class SettingItemView extends LinearLayout {
    private ViewGroup container;
    private TextView tvTitle,tvValue;

    public SettingItemView(Context context) {
        super(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(attrs);
    }

    /**
     * 初始化界面
     */
    private void initUI(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_settings_item,this,true);
        container= (ViewGroup) findViewById(R.id.container_settings);
        tvTitle= (TextView) findViewById(R.id.tv_settings_title);
        tvValue= (TextView) findViewById(R.id.tv_settings_value);

        TypedArray ta=getContext().obtainStyledAttributes(attrs,R.styleable.SettingItemView);
        String title = ta.getString(R.styleable.SettingItemView_title);
        String value= ta.getString(R.styleable.SettingItemView_value);
        ta.recycle();

        tvTitle.setText(title);
        tvValue.setText(value);
    }

    /**
     * 设置条目点击事件
     * @param onClickListener
     */
    private void setSettingItemClickListener(final OnClickListener onClickListener){
        if (container!=null){
            container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v);
                }
            });
        }
    }

    /**
     * 设置标题
     * @param title
     */
    private void setTitle(String title){
        if (tvTitle!=null){
            tvTitle.setText(title);
        }
    }

    /**
     * 设置内容
     * @param value
     */
    private void setTvValue(String value){
        if (tvValue!=null){
            tvTitle.setText(value);
        }
    }
}
