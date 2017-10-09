package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import android.view.View;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */

public interface OnRegularClickListener {
    void onPhoneClickListener(View view, String regexMatcher,int type);
    void onUrlClickListener(View view,String regexMatcher,int type);
    void onEmailClickListener(View view,String regexMatcher,int type);
}
