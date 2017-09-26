package com.skkk.boiledwaternote.CostomViews.RichEdit;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
public class RichEditView extends RelativeLayout implements View.OnClickListener, RichEditable {
    private String TAG = RichEditView.class.getSimpleName();
    private RecyclerView rvRichEdit;
    private LinearLayoutManager linearLayoutManager;
    private List<NoteEditModel> mDataList;
    private NoteEditAdapter adapter;
    private Context context;
    private ImageView ivFormatAlignCenter, ivFormatBold, ivFormatItalic,
            ivFormatList, ivFormatHorSeperate, ivFormatQuote, ivFormatTitle,
            ivFormatUnderLine, ivFormatStrikeThrough,ivFormatCheckBox;
    private ImageView ivEditFormatNotice;

    private NoteEditAdapter.OnImageItemClickListener onImageItemClickListener;

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
        ivFormatHorSeperate = (ImageView) findViewById(R.id.iv_format_hor_seperate);
        ivFormatQuote = (ImageView) findViewById(R.id.iv_format_quote);
        ivFormatTitle = (ImageView) findViewById(R.id.iv_format_title);
        ivFormatUnderLine = (ImageView) findViewById(R.id.iv_format_underlined);
        ivFormatStrikeThrough = (ImageView) findViewById(R.id.iv_format_strike_through);
        ivFormatCheckBox = (ImageView) findViewById(R.id.iv_format_checkbox);

        ivFormatAlignCenter.setOnClickListener(this);
        ivFormatBold.setOnClickListener(this);
        ivFormatItalic.setOnClickListener(this);
        ivFormatList.setOnClickListener(this);
        ivFormatHorSeperate.setOnClickListener(this);
        ivFormatQuote.setOnClickListener(this);
        ivFormatTitle.setOnClickListener(this);
        ivFormatUnderLine.setOnClickListener(this);
        ivFormatStrikeThrough.setOnClickListener(this);
        ivFormatCheckBox.setOnClickListener(this);
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

        adapter.setOnKeyDownFinishListener(new NoteEditAdapter.OnKeyDownFinishListener() {
            @Override
            public void onEnterFinishListner(int pos) {
                /*
                * 锁定光标
                * */
                if (pos < adapter.getItemCount() - 1) {
                    rvRichEdit.smoothScrollToPosition(pos);
                } else {
                    rvRichEdit.smoothScrollToPosition(adapter.getItemCount());
                }
            }

            @Override
            public void onDelFinishListner(int pos) {

            }
        });

        /*
        * Item中EidtText获取到焦点的时候的监听事件
        * */
        adapter.setOnItemEditSelectedLintener(new NoteEditAdapter.OnItemEditHasFocusListener() {
            @Override
            public void onItemEditHasFocusListener(View view, int pos) {
                //在接收到焦点的时候重置底部富文本状态
//                resetBottomBarStatus();
                NoteEditModel model = adapter.getmDataList().get(pos);
                if (model.getItemFlag() == NoteEditModel.Flag.TEXT) {
                    if (model.isFormat_quote() || model.isFormat_list()) {
                        ivFormatAlignCenter.setEnabled(false);
                        ivFormatAlignCenter.setImageResource(R.drawable.format_align_center_unenable);
                    } else {
                        ivFormatAlignCenter.setEnabled(true);
                        ivFormatAlignCenter.setImageResource(R.drawable.format_align_center);
                    }

                }
            }
        });


        rvRichEdit.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View underView = rvRichEdit.findChildViewUnder(event.getX(), event.getY());
                    if (underView == null) {
                        selectLastTextItem(rvRichEdit, linearLayoutManager);
                    }
                }
                return false;
            }
        });

        adapter.setOnImageItemClickListener(onImageItemClickListener);
    }

    /**
     * 获取最后一个Item，如果是文本，那么就获取焦点呼出键盘
     *
     * @param rvNoteEdit
     * @param layoutManager
     */
    private void selectLastTextItem(RecyclerView rvNoteEdit, LinearLayoutManager layoutManager) {
        int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
        View lastItem = rvNoteEdit.getChildAt(lastPos);
        if (lastItem == null) {
            return;
        }
        if (null != rvNoteEdit.getChildViewHolder(lastItem)) {
            NoteEditAdapter.NoteEditViewHolder viewHolder = (NoteEditAdapter.NoteEditViewHolder) rvNoteEdit.getChildViewHolder(lastItem);
            if (viewHolder.etItem.getVisibility() == View.VISIBLE
                    && mDataList.get(mDataList.size() - 1).getItemFlag() == NoteEditModel.Flag.TEXT) {
                //首先判断文本是可见的,那么就获取焦点呼出键盘
                String lastText = viewHolder.etItem.getText().toString();
                viewHolder.etItem.setSelection(lastText.length());
                viewHolder.etItem.setFocusable(true);
                viewHolder.etItem.setFocusableInTouchMode(true);
                viewHolder.etItem.requestFocus();
                viewHolder.etItem.setSelection(viewHolder.etItem.length());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }
    }

    /**
     * 根据是否为新笔记加载内容
     *
     * @param isNew 是否为新建笔记
     * @param datas 如果不是新建笔记那么给出笔记数据集
     */
    public void startNewEdit(boolean isNew, List<NoteEditModel> datas) {
        if (isNew && datas == null) {       //新笔记
            resetRichText();
        } else {                            //加载旧笔记
            loadRichText(datas);
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
     * 设置按键听事件
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
                currentHolder.setFormat_align_center(!currentHolder.isFormat_align_center());
                adapter.notifyItemChanged(currentHolder.getCurrentPos());
                break;
            case R.id.iv_format_blod:                //设置文字Blod
                if (!isSelected) {                   //如果没有选择
                    if (currentHolder.isFormat_bold()) {
                        currentHolder.setFormat_blod(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.setFormat_blod(true);
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
                    if (currentHolder.isFormat_italic()) {
                        currentHolder.setFormat_italic(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.setFormat_italic(true);
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
                /*
                * 点击列表项目之后在下方添加一行
                * */
                currentHolder.setFormat_list(!currentHolder.isFormat_list());

                break;

            case R.id.iv_format_hor_seperate:       //增加分隔线
                List<NoteEditModel> cameraItems = new ArrayList<>();
                cameraItems.add(new NoteEditModel(null, NoteEditModel.Flag.SEPARATED, null));
                cameraItems.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
                insertItems(cameraItems, currentHolder.getCurrentPos()+1);
                adapter.setFocusItemPos(currentHolder.getCurrentPos()+2);
                adapter.notifyDataSetChanged();

                /*
                * 锁定光标
                * */
                if (currentHolder.getCurrentPos() < adapter.getItemCount() - 1) {
                    rvRichEdit.smoothScrollToPosition(currentHolder.getCurrentPos());
                } else {
                    rvRichEdit.smoothScrollToPosition(adapter.getItemCount());
                }
                break;

            case R.id.iv_format_title:
                currentHolder.setFormat_title(!currentHolder.isFormat_title());
                break;

            case R.id.iv_format_quote:
                /*
                * 点击列表项目之后在下方添加一行
                * */
                currentHolder.setFormat_quote(!currentHolder.isFormat_quote());
                break;
            case R.id.iv_format_checkbox:
                /*
                * 点击列表项目之后在下方添加一行
                * */

                currentHolder.setForamtCheckBox(!currentHolder.isForamt_show_checkBox(),false);
                break;

            case R.id.iv_format_underlined:         //设置文字下划线
                if (!isSelected) {
                    if (currentHolder.isFormat_underlined()) {
                        currentHolder.setFormat_underlined(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.setFormat_underlined(true);
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
                    if (currentHolder.isFormat_strike_through()) {
                        currentHolder.setFormat_strike_through(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.setFormat_strike_through(true);
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


    /**
     * 在指定位置添加Item
     *
     * @param itemDatas Items
     * @param pos       指定位置，如果设置为-1，那么默认最后
     */
    public void insertItems(List<NoteEditModel> itemDatas, int pos) {
        if (pos == -1) {
            adapter.getmDataList().addAll(itemDatas);
        } else {
            adapter.getmDataList().addAll(pos, itemDatas);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取当前选中的Item的位置
     * @return
     */
    public int getCurrentHolderPosition(){
        if (null==adapter.getCurrentHolder()){
            return 0;
        }
        return adapter.getCurrentHolder().getCurrentPos();
    }

    public List<NoteEditModel> getCurrentDataList(){
        return adapter.getmDataList();
    }

    /**
     * 重置（开始新的）数据
     */
    @Override
    public void resetRichText() {
        adapter.setmDataList(loadData());
        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshRichText() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载数据
     *
     * @param richTexts
     */
    @Override
    public void loadRichText(List<NoteEditModel> richTexts) {
        adapter.getmDataList().addAll(richTexts);        //同步数据
        adapter.setmDataList(richTexts);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取此时的富文本数据内容
     *
     * @return 数据内容
     */
    @Override
    public List<NoteEditModel> getRichText() {
        return adapter.getmDataList();
    }

    /**
     * 是否获取焦点
     */
    @Override
    public void setFocusEnable(boolean focus) {
        if (focus) {
            adapter.setFocusItemPos(adapter.getItemCount());
        } else {
            ivEditFormatNotice.setFocusable(true);
            ivEditFormatNotice.setFocusableInTouchMode(true);
            ivEditFormatNotice.setFocusable(true);
        }
    }

    /**
     * 重置bottomBar状态
     */
    @Override
    public void resetBottomBarStatus() {
        ivFormatAlignCenter.setBackgroundColor(Color.TRANSPARENT);
        ivFormatBold.setBackgroundColor(Color.TRANSPARENT);
        ivFormatHorSeperate.setBackgroundColor(Color.TRANSPARENT);
        ivFormatItalic.setBackgroundColor(Color.TRANSPARENT);
        ivFormatList.setBackgroundColor(Color.TRANSPARENT);
        ivFormatQuote.setBackgroundColor(Color.TRANSPARENT);
        ivFormatTitle.setBackgroundColor(Color.TRANSPARENT);
        ivFormatStrikeThrough.setBackgroundColor(Color.TRANSPARENT);
        ivFormatUnderLine.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setOnImageItemClickListener(NoteEditAdapter.OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
        adapter.setOnImageItemClickListener(onImageItemClickListener);
    }
}
