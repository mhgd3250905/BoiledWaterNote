package com.skkk.boiledwaternote.CostomViews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.MyItemTouchHelperCallback;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/17.
 */
/*
* 
* 描    述：富文本编辑器
* 作    者：ksheng
* 时    间：2017/6/17$ 23:41$.
*/
public class RichEditView extends RelativeLayout implements View.OnClickListener {
    private String TAG = RichEditView.class.getSimpleName();
    private RecyclerView rvRichEdit;
    private LinearLayoutManager linearLayoutManager;
    private List<NoteEditModel> mDataList;
    private NoteEditAdapter adapter;
    private Context context;
    private ImageView ivFormatAlignCenter, ivFormatBold, ivFormatItalic,
            ivFormatList, ivFormatListNum, ivFormatQuote, ivFormatSize,
            ivFormatUnderLine, ivFormatStrikeThrough;
    private ImageView ivEditFormatNotice;

    private MyItemTouchHelperCallback callback;
    private ItemTouchHelper itemTouchHelper;

    public RichEditView(Context context) {
        super(context);
        initView(context);
    }

    public RichEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RichEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        LayoutInflater.from(this.context).inflate(R.layout.view_rich_edit, this, true);
        initRV();
        initBottomBar();
        ivEditFormatNotice = (ImageView) findViewById(R.id.iv_edit_format_notice);
    }

    /**
     * 初始化底部边框
     */
    private void initBottomBar() {
        ivFormatAlignCenter = (ImageView) findViewById(R.id.iv_format_align_center);
        ivFormatBold = (ImageView) findViewById(R.id.iv_format_blod);
        ivFormatItalic = (ImageView) findViewById(R.id.iv_format_italic);
        ivFormatList = (ImageView) findViewById(R.id.iv_format_list);
        ivFormatListNum = (ImageView) findViewById(R.id.iv_format_list_numbered);
        ivFormatQuote = (ImageView) findViewById(R.id.iv_format_quote);
        ivFormatSize = (ImageView) findViewById(R.id.iv_format_size);
        ivFormatUnderLine = (ImageView) findViewById(R.id.iv_format_underlined);
        ivFormatStrikeThrough = (ImageView) findViewById(R.id.iv_format_strike_through);

        ivFormatAlignCenter.setOnClickListener(this);
        ivFormatBold.setOnClickListener(this);
        ivFormatItalic.setOnClickListener(this);
        ivFormatList.setOnClickListener(this);
        ivFormatListNum.setOnClickListener(this);
        ivFormatQuote.setOnClickListener(this);
        ivFormatSize.setOnClickListener(this);
        ivFormatUnderLine.setOnClickListener(this);
        ivFormatStrikeThrough.setOnClickListener(this);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRV() {
        rvRichEdit = (RecyclerView) findViewById(R.id.rv_rich_edit);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRichEdit.setLayoutManager(linearLayoutManager);
        mDataList = loadData();
        adapter = new NoteEditAdapter(context, mDataList);
        rvRichEdit.setAdapter(adapter);
        rvRichEdit.setItemAnimator(new DefaultItemAnimator());
        //设置ItemTouchHelper
        callback = new MyItemTouchHelperCallback(context, adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvRichEdit);

        //设置Item拖拽监听
        adapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDragListener(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
    }

    /**
     * 根据是否为新笔记加载内容
     *
     * @param isNew
     * @param note
     */
    private void startNewEdit(boolean isNew, List<NoteEditModel> note) {
        if (isNew && note != null) {     //新笔记
            adapter.setmDataList(loadData());
            adapter.notifyDataSetChanged();
        } else {                     //加载旧笔记
            adapter.setmDataList(note);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载数据
     * 这里默认加载一个文本类型的空数据
     */
    private List<NoteEditModel> loadData() {
        List<NoteEditModel> dates = new ArrayList<>();
        dates.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
        return dates;
    }


    /**
     * 设施按键听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        NoteEditAdapter.NoteEditViewHolder currentHolder = adapter.getCurrentHolder();
        if (currentHolder == null) {
            return;
        }
        boolean isSelected = currentHolder.etItem.hasSelection();
        int start = currentHolder.etItem.getSelectionStart();
        int end = currentHolder.etItem.getSelectionEnd();

        startBottomViewAnim(v);

        switch (v.getId()) {
            /*
            * 设置文字居中
            * */
            case R.id.iv_format_align_center:
                break;
            case R.id.iv_format_blod:                //设置文字Blod
                if (!isSelected) {                   //如果没有选择
                    if (currentHolder.myItemTextChangeListener.isFormat_blod()) {
                        currentHolder.myItemTextChangeListener.setFormat_blod(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_blod(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {                             //如果选择
                    //获取选择区域内所有的StyleSpan
                    StyleSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StyleSpan.class);
                    List<StyleSpan> hasSpans = new ArrayList<>();
                    for (int i = 0; i < spans.length; i++) {
                        if (spans[i].getStyle() == Typeface.BOLD) {
                            hasSpans.add(spans[i]);
                        }
                    }
                    if (hasSpans.size() != 0) {   //如果有Bold则设置为正常
                        for (StyleSpan span : hasSpans) {
                            currentHolder.removeSpan(span);
                        }
                    } else {                    //如果不包含Bold那么就设置粗体
                        currentHolder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_italic:             //设置文字斜体
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_italic()) {
                        currentHolder.myItemTextChangeListener.setFormat_italic(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_italic(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    StyleSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StyleSpan.class);
                    List<StyleSpan> hasSpans = new ArrayList<>();
                    for (int i = 0; i < spans.length; i++) {
                        if (spans[i].getStyle() == Typeface.ITALIC) {
                            hasSpans.add(spans[i]);
                        }
                    }
                    if (hasSpans.size() != 0) {   //如果有ITALIC则设置为正常
                        for (StyleSpan span : hasSpans) {
                            currentHolder.removeSpan(span);
                        }
                    } else {               //如果不包含ITALIC那么就设置斜体
                        currentHolder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_list:               //列表
                if (adapter.isItemFormatList()) {
                    adapter.setItemFormatList(false);
                } else {
                    adapter.setItemFormatList(true);
                }
                v.setBackgroundColor(adapter.isItemFormatList() ? Color.LTGRAY : Color.TRANSPARENT);
                break;
            case R.id.iv_format_list_numbered:
                break;
            case R.id.iv_format_size:
                break;
            case R.id.iv_format_quote:
                if (currentHolder.myItemTextChangeListener.isFormat_quote()) {
                    currentHolder.setFormat_quote(false);
                } else {
                    currentHolder.setFormat_quote(true);
                }
                break;
            case R.id.iv_format_underlined:         //设置文字下划线
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_underlined()) {
                        currentHolder.myItemTextChangeListener.setFormat_underlined(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_underlined(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    UnderlineSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, UnderlineSpan.class);
                    boolean hasSpan = false;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans.length > 0) {
                            hasSpan = true;
                        }
                    }
                    //清除区域内所有的UnderLineSpan
                    for (int i = 0; i < spans.length; i++) {
                        currentHolder.removeSpan(spans[i]);
                    }
                    //如果本身没有Span，这里需要设置
                    if (!hasSpan) {
                        currentHolder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_strike_through:     //设置文字删除线
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_strike_through()) {
                        currentHolder.myItemTextChangeListener.setFormat_strike_through(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_strike_through(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    StrikethroughSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StrikethroughSpan.class);
                    boolean hasSpan = false;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans.length > 0) {
                            hasSpan = true;
                        }
                    }
                    //清除区域内所有的UnderLineSpan
                    for (int i = 0; i < spans.length; i++) {
                        currentHolder.removeSpan(spans[i]);
                    }
                    //如果本身没有Span，这里需要设置
                    if (!hasSpan) {
                        currentHolder.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
        }
        startBottomViewAnim(v);
    }

    /**
     * bottomBar的按键动画
     *
     * @param v
     */
    private void startBottomViewAnim(View v) {
        ivEditFormatNotice.setVisibility(View.VISIBLE);
        if (v instanceof ImageView) {
            Drawable drawable = ((ImageView) v).getDrawable();
            ivEditFormatNotice.setImageDrawable(drawable);
        }
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(ivEditFormatNotice,
                "scaleX", 1f, 1.5f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(ivEditFormatNotice,
                "scaleY", 1f, 1.5f);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(ivEditFormatNotice,
                "alpha", 1f, 0);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnim).with(scaleYAnim).with(alphaAnim);
        set.setDuration(500);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivEditFormatNotice.setVisibility(View.GONE);
            }
        });
        set.start();

    }

    public void insertItems(List<NoteEditModel> itemDatas,int pos){
        if (pos==-1) {
            mDataList.addAll(adapter.getCurrentHolder().getCurrentPos()+1, itemDatas);
        }else {
            mDataList.addAll(pos+1, itemDatas);
        }
        adapter.setmDataList(mDataList);
        adapter.notifyDataSetChanged();
    }

}
