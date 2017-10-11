package com.skkk.boiledwaternote.CostomViews.RichEdit;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.skkk.boiledwaternote.CostomViews.ClickableEdit.ClickableEditText;

/**
 * 创建于 2017/10/11
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/10/11$ 21:12$.
*/
public class FormatTextChangeWatcher implements TextWatcher {
    private boolean format_blod = false;          //加粗
    private boolean format_italic = false;        //斜体
    private int format_size = 0;                  //字体大小：0-p 1-h1 2-h2 3-h3
    private boolean format_underlined = false;    //下划线
    private boolean format_strike_through = false;//删除线
    private int lastPos;

    private ClickableEditText editText;
    private FormatTextChangeToDoListener formatTextChangeToDoListener;

    public interface FormatTextChangeToDoListener {
        void formatTextChangeToDoListener(CharSequence s,int selectionIndex);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        editText.removeTextChangedListener(this);

        SpannableString ss = new SpannableString(s);
        //设置斜体和粗体
        if (format_blod && format_italic) {
            for (int i = start; i < start + count; i++) {
                ss.setSpan(new StyleSpan(Typeface.BOLD), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new StyleSpan(Typeface.ITALIC), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (format_blod) {
            for (int i = start; i < start + count; i++) {
                ss.setSpan(new StyleSpan(Typeface.BOLD), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (format_italic) {
            for (int i = start; i < start + count; i++) {
                ss.setSpan(new StyleSpan(Typeface.ITALIC), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            StyleSpan[] spans = ss.getSpans(0, ss.length(), StyleSpan.class);
            if (spans.length != 0) {   //如果有ITALIC则设置为正常
                for (StyleSpan span : spans) {
                    ss.removeSpan(span);
                }
            }
        }

        //设置下划线
        if (format_underlined) {
            for (int i = start; i < start + count; i++) {
                ss.setSpan(new UnderlineSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            UnderlineSpan[] spans = ss.getSpans(0, ss.length(), UnderlineSpan.class);
            if (spans.length != 0) {   //如果有ITALIC则设置为正常
                for (UnderlineSpan span : spans) {
                    ss.removeSpan(span);
                }
            }
        }

        //设置删除线
        if (format_strike_through) {
            for (int i = start; i < start + count; i++) {
                ss.setSpan(new StrikethroughSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            StrikethroughSpan[] spans = ss.getSpans(0, ss.length(), StrikethroughSpan.class);
            if (spans.length != 0) {   //如果有ITALIC则设置为正常
                for (StrikethroughSpan span : spans) {
                    ss.removeSpan(span);
                }
            }
        }

        editText.setText(ss);

        if (formatTextChangeToDoListener!=null){
            formatTextChangeToDoListener.formatTextChangeToDoListener(ss,(start + count) > 0 ? start + count : 0);
        }

        editText.addTextChangedListener(this);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public void setFormat_blod(boolean format_blod) {
        this.format_blod = format_blod;
    }

    public void setFormat_italic(boolean format_italic) {
        this.format_italic = format_italic;
    }

    public void setFormat_size(int format_size) {
        this.format_size = format_size;
    }

    public void setFormat_underlined(boolean format_underlined) {
        this.format_underlined = format_underlined;
    }

    public void setFormat_strike_through(boolean format_strike_through) {
        this.format_strike_through = format_strike_through;
    }

    public boolean isFormat_blod() {
        return format_blod;
    }

    public boolean isFormat_italic() {
        return format_italic;
    }


    public int getFormat_size() {
        return format_size;
    }

    public boolean isFormat_strike_through() {
        return format_strike_through;
    }

    public boolean isFormat_underlined() {
        return format_underlined;
    }

    public void initWatcher(ClickableEditText editText,FormatTextChangeToDoListener formatTextChangeToDoListener) {
        this.editText = editText;
        this.formatTextChangeToDoListener=formatTextChangeToDoListener;
    }

}
