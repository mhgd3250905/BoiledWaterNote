package com.skkk.boiledwaternote.CostomViews.RichEdit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 创建于 2017/9/27
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/27$ 23:47$.
*/
public class SelectionEditText extends EditText {
    private static final String TAG = "SelectionEditText";
    private OnSelectionChangeListener onSelectionChangeListener;

    public interface OnSelectionChangeListener {
        void onSelectionChangeListener(int selStart, int selEnd);
    }

    public SelectionEditText(Context context) {
        super(context);
    }

    public SelectionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        Log.i(TAG, "onSelectionChanged: selStart: "+selStart+",selEnd: "+selEnd);
        super.onSelectionChanged(selStart, selEnd);
        if (onSelectionChangeListener!=null){
            onSelectionChangeListener.onSelectionChangeListener(selStart,selEnd);
        }
    }
}
