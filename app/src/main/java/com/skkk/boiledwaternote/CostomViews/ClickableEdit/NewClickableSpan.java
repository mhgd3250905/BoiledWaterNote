package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import android.text.TextPaint;
import android.view.View;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/30$ 19:55$.
*/
public class NewClickableSpan extends android.text.style.ClickableSpan {
    private View.OnClickListener onClickListener;
    private int textColor;

    public NewClickableSpan(int textColor,View.OnClickListener onClickListener) {
        this.textColor = textColor;
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View widget) {
        onClickListener.onClick(widget);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(textColor);
    }

}