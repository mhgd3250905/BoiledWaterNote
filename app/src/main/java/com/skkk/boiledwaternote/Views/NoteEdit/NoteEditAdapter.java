package com.skkk.boiledwaternote.Views.NoteEdit;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skkk.boiledwaternote.CostomViews.BombMenu;
import com.skkk.boiledwaternote.CostomViews.OnMenuItemClickListener;
import com.skkk.boiledwaternote.CostomViews.OnMenuItemTouchListener;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.ItemTouchHelperAdapter;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.ImageUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.N;
import static android.view.View.GONE;

/**
 * Created by admin on 2017/4/22.
 */
/*
*
* 描    述：数据适配器
* 作    者：ksheng
* 时    间：2017/4/22$ 22:34$.
*/
public class NoteEditAdapter extends RecyclerView.Adapter<NoteEditAdapter.NoteEditViewHolder> implements ItemTouchHelperAdapter {
    private Context context;                                                    //上下文
    private List<NoteEditModel> mDataList;                                      //数据
    private OnStartDragListener onStartDragListener;                            //拖拽监听
    private OnItemEditHasFocusListener onItemEditHasFocusListener;              //Item编辑焦点监听
    private OnKeyDownFinishListener onKeyDownFinishListener;                    //换行删除按键处理完毕监听
    private NoteEditViewHolder currentHolder;                                   //当前ViewHolder
    private boolean alignCenterText = false;                                    //ItemEdit文字是否居中对齐
    private int focusItemPos = -1;                                              //需要获得焦点的Item
    private boolean itemFormatList = false;                                     //设置List格式为List
    private int separatedImageResouseId;


    public interface OnItemEditHasFocusListener {
        void onItemEditHasFocusListener(View view, int pos);
    }



    public interface OnKeyDownFinishListener {
        void onEnterFinishListner(int pos);

        void onDelFinishListner(int pos);
    }




    public NoteEditAdapter(Context context, List<NoteEditModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public NoteEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note_edit, parent, false);
        NoteEditViewHolder viewHolder
                = new NoteEditViewHolder(v, new MyItemTextChangeListener());
        return viewHolder;
    }

    public List<NoteEditModel> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<NoteEditModel> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public void onBindViewHolder(final NoteEditViewHolder holder, final int position) {
        final NoteEditViewHolder viewHolder = holder;
        holder.setCurrentPos(holder.getAdapterPosition());          //设置当前Item位置
        holder.myItemTextChangeListener.updatePos(position);        //更新文本变化监听pos
        if (onKeyDownFinishListener != null) {                        //设置按键事件完成回调
            holder.setOnKeyDownFinishListener(onKeyDownFinishListener);
        }


        NoteEditModel itemDate = mDataList.get(position);             //获取dateBean

        //判断我们加载的到底是什么类型的数据
        if (itemDate.getItemFlag() == NoteEditModel.Flag.TEXT) {     //如果是文本Item
            /*
            * 文本
            * */
            //显示内容

            //因为HTML转Span之后块级元素会自动换行，所以这里直接手动将块级元素P删除掉，有点暴力
            //通过将<p>元素替换为<span>元素来避免以为块级元素自动添加换号引起的麻烦
            String contentHtml = mDataList.get(position).getContent();

            contentHtml = contentHtml.replace("\n", "").replace("/p><p","/span><br><span").replace("<p", "<span").replace("/p>", "/span>");

            if (Build.VERSION.SDK_INT >= N) {
                holder.etItem.setText(Html.fromHtml(contentHtml, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.etItem.setText(Html.fromHtml(contentHtml));
            }

            //设置光标在最后一个字符后面，默认会多出一个空格
            if (!TextUtils.isEmpty(mDataList.get(position).getContent())) {
                holder.etItem.setSelection(holder.etItem.length() - 1);
            }

            //设置编辑文本框的焦点变化监听，谁获得焦点我们就获取到当前获取焦点的Holder
            holder.etItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        currentHolder = viewHolder;
                    }
                    if (onItemEditHasFocusListener!=null&&hasFocus){
                        onItemEditHasFocusListener.onItemEditHasFocusListener(v,position);
                    }
                }
            });


            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
            holder.llEditContainer.setVisibility(View.VISIBLE);          //文本显示 图片隐藏
            holder.rlItemImg.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(GONE);


            //设置指定的Item获取焦点

            if (focusItemPos == position) {
                holder.etItem.setFocusable(true);
                holder.etItem.setFocusableInTouchMode(true);
                holder.etItem.requestFocus();
                holder.etItem.setSelection(holder.etItem.length());
            }

            //设置引用格式
            holder.setFormat_quote(itemDate.isFormat_quote());
            //设置列表格式
            holder.setFormat_list(itemDate.isFormat_list());
            //设置对齐方式
            Log.i(TAG, "onBindViewHolder: pos->"+position+"center?->"+itemDate.isFormat_align_center());
            holder.setFormat_align_flag(itemDate.isFormat_align_center());



        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.IMAGE) {//如果是图片Item
            /*
            * 图片
            * */
            holder.rlItemImg.setVisibility(View.VISIBLE);
            holder.llEditContainer.setVisibility(GONE);
            holder.ivTextQuote.setVisibility(GONE);
            holder.ivTextPonit.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(GONE);

            holder.bmItemImage.setmMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public void onItemClickListener(int pos, View v) {
                    switch (pos){
                        case 0:
                            break;
                        case 1:
                            mDataList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,getItemCount());
                            break;
                    }
                }
            });

            holder.bmItemImage.setmMenuItemTouchListener(new OnMenuItemTouchListener() {
                @Override
                public void onItemTouchListener(int pos, View v) {
                    //当触摸到滑动按钮的时候
                    onStartDragListener.onStartDragListener(viewHolder);
                }

                @Override
                public void onItemTouchLeaveListener(int pos, View v) {

                }
            });


            if (itemDate.getImagePath() == null) {
                return;
            }


                        /*
            * 根据图片的宽高来设置相框的大小
            * */
            int imgWidth= ImageUtils.getBitmapWidth(itemDate.getImagePath(),true);
            int imgHeight=ImageUtils.getBitmapWidth(itemDate.getImagePath(),false);

            ViewGroup.LayoutParams layoutParams =  holder.ivItemImage.getLayoutParams();
            if (imgHeight>imgWidth) {
                layoutParams.width = (int) context.getResources().getDimension(R.dimen.item_edit_image_width_ver);
                layoutParams.height =(int)context.getResources().getDimension(R.dimen.item_edit_image_height_ver);
            }else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = (int)context.getResources().getDimension(R.dimen.item_edit_image_height_ver);
            }
            holder.flBombMenuContainer.setLayoutParams(layoutParams);
            holder.ivItemImage.setLayoutParams(layoutParams);
            holder.ivItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);



            Glide.with(context)
                    .load(itemDate.getImagePath())
                    .into(holder.ivItemImage);

        }else if (itemDate.getItemFlag()== NoteEditModel.Flag.SEPARATED){
            /*
            * 分隔线
            * */
            holder.rlItemImg.setVisibility(GONE);
            holder.llEditContainer.setVisibility(GONE);
            holder.ivTextQuote.setVisibility(GONE);
            holder.ivTextPonit.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 上下拖动替换item位置
     *
     * @param fromPos
     * @param toPos
     */
    @Override
    public void onItemMove(int fromPos, int toPos) {
        Collections.swap(mDataList, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    /**
     * 替换Item位置完毕
     */
    @Override
    public void onItemMoveDone() {
        notifyDataSetChanged();
    }

    /**
     * 左右滑动删除Item
     *
     * @param pos
     */
    @Override
    public void onitemSwipe(int pos) {
        mDataList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());
    }

    /**
     * 设置哪个位置的Item获取焦点
     * @param focusItemPos
     */
    public void setFocusItemPos(int focusItemPos) {
        this.focusItemPos = focusItemPos;
        notifyDataSetChanged();
    }

    /**
     * 设置Item编辑器焦点获取监听
     *
     * @param onItemEditSelectedLintener
     */
    public void setOnItemEditSelectedLintener(OnItemEditHasFocusListener onItemEditSelectedLintener) {
        this.onItemEditHasFocusListener = onItemEditSelectedLintener;
    }

    public void setOnKeyDownFinishListener(OnKeyDownFinishListener onKeyDownFinishListener) {
        this.onKeyDownFinishListener = onKeyDownFinishListener;
    }


    /**
     * 设置滑动拖拽监听
     *
     * @param onStartDragListener
     */
    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    /**
     * 获取Item编辑器是否居中
     *
     * @return
     */
    public boolean isAlignCenterText() {
        return alignCenterText;
    }

    /**
     * 设置Item编辑器是否居中对齐
     *
     * @param alignCenterText
     */
    public void setTextAligentCenter(boolean alignCenterText) {
        this.alignCenterText = alignCenterText;
        currentHolder.setFormat_align_flag(this.alignCenterText);
        mDataList.get(currentHolder.getCurrentPos()).setFormat_align_center(this.alignCenterText);
        notifyItemChanged(currentHolder.getCurrentPos());
    }




    public boolean isItemFormatList() {
        return itemFormatList;
    }


    public void setItemFormatList(boolean itemFormatList) {
        this.itemFormatList = itemFormatList;
        notifyDataSetChanged();
    }


    /**
     * ViewHolder
     */
    public class NoteEditViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_edit_container)
        public LinearLayout llEditContainer;    //edit文本区域
        @Bind(R.id.tv_item_recylcer)
        public EditText etItem;                 //编辑文本框
        @Bind(R.id.iv_text_quote)
        public ImageView ivTextQuote;           //编辑栏位引用图标
        @Bind(R.id.iv_text_point)
        public ImageView ivTextPonit;           //编辑栏位列表图标

        @Bind(R.id.rl_item_img)
        public RelativeLayout rlItemImg;        //Image区域容器
        @Bind(R.id.iv_item_img)
        public ImageView ivItemImage;           //图片框
        @Bind(R.id.cv_item_img)
        public CardView cvItemImg;              //图片容器
        @Bind(R.id.bm_item_image)
        public BombMenu bmItemImage;            //弹射菜单
        @Bind(R.id.fl_menu_container)
        public FrameLayout flBombMenuContainer; //弹射菜单容器

        private Boolean moveMenuIsHide=true;

        @Bind(R.id.iv_swipe_notice)
        public View ivSwipeNotice;              //拖拽切换的时候的提示图标

        @Bind(R.id.rl_item_separated)
        public RelativeLayout rlItemSeparated;  //分割线容器


        private OnKeyDownFinishListener onKeyDownFinishListener;

        private int currentPos;             //当前的position
        private NoteEditModel model;        //用来操作的模板

        public int getCurrentPos() {
            return currentPos;
        }

        public void setCurrentPos(int currentPos) {
            this.currentPos = currentPos;
        }

        public MyItemTextChangeListener myItemTextChangeListener;

        public NoteEditViewHolder(View itemView, final MyItemTextChangeListener myItemTextChangeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llEditContainer.setFocusable(true);
            llEditContainer.setFocusableInTouchMode(true);
            //设置焦点获取监听：直接抛给EditText
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (etItem.getVisibility() == View.VISIBLE) {
                        if (hasFocus) {
                            etItem.setSelection(etItem.length());
                        }
                    }
                }
            });
            this.myItemTextChangeListener = myItemTextChangeListener;
            myItemTextChangeListener.setCurrentEdit(etItem);
            etItem.addTextChangedListener(myItemTextChangeListener);        //当文本发生变化的时候就保存对应的内容到dataList

            /**
             * 设置Edit按键监听
             */
            etItem.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (myItemTextChangeListener.isFormat_list() || myItemTextChangeListener.isFormat_quote()) {
                            //将要插入的行数据
                            model = new NoteEditModel();
                            model.setItemFlag(NoteEditModel.Flag.TEXT);
                            model.setImagePath(null);
                            model.setContent("");
                            //根据富文本格式不同设置不同的样式
                            if (myItemTextChangeListener.isFormat_list()) {    //如果是列表格式
                                if (etItem.length() > 0) {
                                    //当这个列表中有内容的时候，换行下一个才会是列表格式
                                    //否则下一个就不是列表格式
                                    model.setFormat_list(myItemTextChangeListener.isFormat_list());
                                    model.setFormat_quote(!myItemTextChangeListener.isFormat_list());
                                }
                            }
                            mDataList.add(currentPos + 1, model);
                            setFocusItemPos(currentPos + 1);
                            notifyDataSetChanged();

                            if (onKeyDownFinishListener != null) {
                                onKeyDownFinishListener.onEnterFinishListner(currentPos + 1);
                            }
                        }
                    }
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (TextUtils.isEmpty(etItem.getText())) {
                            if (myItemTextChangeListener.isFormat_list()) {
                                //如果该item富文本为list那么就取消其状态
                                setFormat_list(!myItemTextChangeListener.isFormat_list());
                                return false;
                            } else if (myItemTextChangeListener.isFormat_quote()) {
                                //如果该item富文本为quote那么就取消其状态
                                setFormat_quote(!myItemTextChangeListener.isFormat_quote());
                                return false;
                            }
                            if (currentPos != 0) {
                                //如果是Eidt已经空了，那么继续按下DEL按钮就删除当前Item，焦点跳转到上一个Item
                                mDataList.remove(currentPos);
                                if (mDataList.get(currentPos-1).getItemFlag()== NoteEditModel.Flag.SEPARATED){
                                    mDataList.remove(currentPos-1);
                                    setFocusItemPos(currentPos - 2);
                                }else {
                                    setFocusItemPos(currentPos - 1);
                                }
                                notifyDataSetChanged();

                                if (onKeyDownFinishListener != null) {
                                    onKeyDownFinishListener.onDelFinishListner(currentPos - 1);
                                }
                            }
                        }
                    }
                    return false;
                }
            });

            etItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return myItemTextChangeListener.isFormat_list() || myItemTextChangeListener.isFormat_quote();
                }
            });

        }

        /**
         * 设置Holder中的编辑栏span特效并同步保存数据样式
         *
         * @param what
         * @param start
         * @param end
         * @param flags
         */
        public void setSpan(Object what, int start, int end, int flags) {
            etItem.getText().setSpan(what, start, end, flags);
            if (android.os.Build.VERSION.SDK_INT >= N) {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
            } else {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText()));
            }
        }

        /**
         * 删除Holder中的编辑框的富文本特效并同步保存数据
         *
         * @param what
         */
        public void removeSpan(Object what) {
            etItem.getText().removeSpan(what);
            if (android.os.Build.VERSION.SDK_INT >= N) {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
            } else {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText()));
            }
        }


        public void setOnKeyDownFinishListener(OnKeyDownFinishListener onKeyDownFinishListener) {
            this.onKeyDownFinishListener = onKeyDownFinishListener;
        }

        /**
         * 设置文本对齐方式
         *
         * @param isTextCenter
         */

        public void setFormat_align_flag(boolean isTextCenter) {
            if (isTextCenter) {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
            myItemTextChangeListener.setFormat_align_center(isTextCenter);
        }

        public void setFormat_blod(boolean format_blod) {
            myItemTextChangeListener.setFormat_blod(format_blod);
        }

        public void setFormat_italic(boolean format_italic) {
            myItemTextChangeListener.setFormat_italic(format_italic);
        }

        public void setFormat_list(boolean format_list) {
            if (format_list) {
                setFormat_quote(!format_list);
            }
            myItemTextChangeListener.setFormat_list(format_list);
            ivTextPonit.setVisibility(format_list ? View.VISIBLE : GONE);
        }

        public void setFormat_list_numbered(boolean format_list_numbered) {
            myItemTextChangeListener.setFormat_list_numbered(format_list_numbered);
        }

        public void setFormat_quote(boolean format_quote) {
            if (format_quote) {
                setFormat_list(!format_quote);
            }
            myItemTextChangeListener.setFormat_quote(format_quote);
            ivTextQuote.setVisibility(format_quote ? View.VISIBLE : GONE);
            etItem.setTextColor(format_quote ? ContextCompat.getColor(context, R.color.colorGray)
                    : ContextCompat.getColor(context, R.color.colorBlackBody));
        }


        public void setFormat_size(int format_size) {
            myItemTextChangeListener.setFormat_size(format_size);
        }

        public void setFormat_underlined(boolean format_underlined) {
            myItemTextChangeListener.setFormat_underlined(format_underlined);
        }

        public Boolean getMoveMenuIsHide() {
            return moveMenuIsHide;
        }

    }


    public class MyItemTextChangeListener implements TextWatcher {
        private int position;

        private EditText currentEdit;

        private boolean flagIsAuto = false;           //设置一个flag用来避免重新设置EditText时候触发监听

        private boolean format_align_center = false;   //0-左 1-中后 2-右
        private boolean format_blod = false;          //加粗
        private boolean format_italic = false;        //斜体
        private boolean format_list = false;          //列表
        private boolean format_list_numbered = false; //数字列表
        private boolean format_quote = false;         //引用
        private int format_size = 0;                  //字体大小：0-p 1-h1 2-h2 3-h3
        private boolean format_underlined = false;    //下划线
        private boolean format_strike_through = false;//删除线


        public void updatePos(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!flagIsAuto) {
                SpannableString ss = new SpannableString(s);
                flagIsAuto = true;
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
                }

                //设置下划线
                if (format_underlined) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new UnderlineSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //设置删除线
                if (format_strike_through) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new StrikethroughSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //设置字体
                if (format_size!=0){
                    switch (format_size){
                        case 1:
                            for (int i = start; i < start + count; i++) {
                                ss.setSpan(new RelativeSizeSpan(4), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            break;
                        case 2:
                            for (int i = start; i < start + count; i++) {
                                ss.setSpan(new RelativeSizeSpan(3), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            break;
                        case 3:
                            for (int i = start; i < start + count; i++) {
                                ss.setSpan(new RelativeSizeSpan(2), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            break;
                    }
                }

                currentEdit.setText(ss);
                currentEdit.setSelection(start + count);

                if (android.os.Build.VERSION.SDK_INT >= N) {
                    Log.i(TAG, "onTextChanged: --->"+Html.toHtml(currentEdit.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                    mDataList.get(position).setContent(Html.toHtml(currentEdit.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                } else {
                    mDataList.get(position).setContent(Html.toHtml(currentEdit.getText()));
                }

            } else {
                flagIsAuto = false;
            }


//            mDataList.set(position, model);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        public void setCurrentEdit(EditText currentEdit) {
            this.currentEdit = currentEdit;
        }

        public void setFormat_align_center(boolean format_align_center) {
            this.format_align_center = format_align_center;
        }

        public void setFormat_blod(boolean format_blod) {
            this.format_blod = format_blod;
        }

        public void setFormat_italic(boolean format_italic) {
            this.format_italic = format_italic;
        }

        public void setFormat_list(boolean format_list) {
            this.format_list = format_list;
            mDataList.get(position).setFormat_list(format_list);
        }

        public void setFormat_list_numbered(boolean format_list_numbered) {
            this.format_list_numbered = format_list_numbered;
        }

        public void setFormat_quote(boolean format_quote) {
            this.format_quote = format_quote;
            mDataList.get(position).setFormat_quote(format_quote);
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

        public boolean isFormat_align_center() {
            return format_align_center;
        }

        public boolean isFormat_blod() {
            return format_blod;
        }

        public boolean isFormat_italic() {
            return format_italic;
        }

        public boolean isFormat_list() {
            return format_list;
        }

        public boolean isFormat_list_numbered() {
            return format_list_numbered;
        }

        public boolean isFormat_quote() {
            return format_quote;
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
    }

    /**
     * 获取当前选中的ViewHolder
     *
     * @return
     */
    public NoteEditViewHolder getCurrentHolder() {
        return currentHolder;
    }
}
