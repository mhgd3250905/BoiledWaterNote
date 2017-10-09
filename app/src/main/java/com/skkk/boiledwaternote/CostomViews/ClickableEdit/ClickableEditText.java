package com.skkk.boiledwaternote.CostomViews.ClickableEdit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 创建于 2017/9/30
 * 作者 admin
 */
/*
* 
* 描    述：自己识别内容的可以点击的编辑文本框
* 作    者：ksheng
* 时    间：2017/9/30$ 19:50$.
*/
public class ClickableEditText extends EditText {
    private RegularUtils regularUtils;
    private OnRegularClickListener regularClickListener;

    private TextWatcher watcher = new TextWatcher() {
        private boolean needRefresh = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            removeTextChangedListener(watcher);
            if (regularUtils != null) {
                List<RegularBean> results = regularUtils.getMatchResults(s);
                SpannableStringBuilder ss = getRegularSpannableString(s, results);
                setText(ss);
                setMovementMethod(LinkMovementMethod.getInstance());
                setSelection(start + count);
            }
            addTextChangedListener(watcher);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * 根据文本获取对应的检索对象
     *
     * @param s
     * @param results
     * @return
     */
    @NonNull
    private SpannableStringBuilder getRegularSpannableString(CharSequence s, final List<RegularBean> results) {
        SpannableStringBuilder ss = new SpannableStringBuilder(s);
        ss.clearSpans();
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        for (int i = 0; i < results.size(); i++) {
            final RegularBean result = results.get(i);
            NewClickableSpan newClickableSpan=null;
            if (result.getType() == RegexParser.phoneType) {
                newClickableSpan= new NewClickableSpan(Color.GREEN, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: PHONE");
                        if (regularClickListener != null) {
                            regularClickListener.onPhoneClickListener(v, result.getMatchContent(),result.getType());
                        }
                    }
                });
            }else if (result.getType()==RegexParser.urlType){
                newClickableSpan= new NewClickableSpan(Color.BLUE, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: URL");
                        if (regularClickListener!=null){
                            regularClickListener.onUrlClickListener(v,result.getMatchContent(),result.getType());
                        }
                    }
                });

            }else if (result.getType()==RegexParser.emailType){
                newClickableSpan= new NewClickableSpan(Color.LTGRAY, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: EMAIL");
                        if (regularClickListener!=null){
                            regularClickListener.onEmailClickListener(v,result.getMatchContent(),result.getType());
                        }
                    }
                });
            }
                ss.setSpan(newClickableSpan,
                    results.get(i).getStart(),
                    results.get(i).getEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public ClickableEditText(Context context) {
        super(context);
        initUI();
    }

    public ClickableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public ClickableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        regularUtils = new RegularUtils();
        RegularRuleBean rule = new RegularRuleBean();
        rule.addNewRule(RegexParser.PHONE_PATTERN, RegexParser.phoneType)
                .addNewRule(RegexParser.URL_PATTERN, RegexParser.urlType)
                .addNewRule(RegexParser.EMAIL_PATTERN, RegexParser.emailType);
        regularUtils.setRegularRule(rule);
        addTextChangedListener(watcher);
    }

    public OnRegularClickListener getRegularClickListener() {
        return regularClickListener;
    }

    public void setRegularClickListener(OnRegularClickListener regularClickListener) {
        this.regularClickListener = regularClickListener;
    }
}






