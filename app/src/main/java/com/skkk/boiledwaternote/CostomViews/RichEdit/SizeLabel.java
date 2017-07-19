package com.skkk.boiledwaternote.CostomViews.RichEdit;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import org.xml.sax.XMLReader;

/**
 * 创建于 2017/7/15
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/7/15$ 21:42$.
*/
public class SizeLabel implements Html.TagHandler {
    private int size;
    private int startIndex = 0;
    private int stopIndex = 0;
    public SizeLabel(int size) {
        this.size = size;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.toLowerCase().equals("size")) {
            if(opening){
                startIndex = output.length();
            }else{
                stopIndex = output.length();
                output.setSpan(new AbsoluteSizeSpan(size), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

}


