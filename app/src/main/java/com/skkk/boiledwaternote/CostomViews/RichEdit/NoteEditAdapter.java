package com.skkk.boiledwaternote.CostomViews.RichEdit;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.ClickableEdit.ClickableEditText;
import com.skkk.boiledwaternote.CostomViews.ClickableEdit.OnRegularClickListener;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.ItemTouchHelperAdapter;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.ImageUtils;
import com.skkk.boiledwaternote.Utils.Utils.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

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
    private OnImageItemClickListener onImageItemClickListener;
    private OnKeyDownFinishListener onKeyDownFinishListener;                    //换行删除按键处理完毕监听
    private NoteEditViewHolder currentHolder;                                   //当前ViewHolder
    private boolean alignCenterText = false;                                    //ItemEdit文字是否居中对齐
    private int focusItemPos = -1;                                              //需要获得焦点的Item
    private boolean itemFormatList = false;                                     //设置List格式为List
    private int separatedImageResouseId;
    private int moveToPos;
    private Configs.OnSelectionChangeListener onSelectionChangeListener;
    private OnRegularClickListener onRegularClickListener;
    private OnEditTextChangeListener onEditTextChangeListener;
    private List<NoteEditModel> lastModles = new ArrayList<>();
    private boolean isHistoryRefresh = false;

    private List<NoteEditModel> lastModels;


    public interface OnImageItemClickListener {
        void onImageClickListener(int pos, View v, NoteEditModel noteEditModel);
    }


    public interface OnItemEditHasFocusListener {
        void onItemEditHasFocusListener(View view, int pos);
    }

    public interface OnKeyDownFinishListener {
        void onEnterFinishListner(int pos);
        void onDelFinishListner(int pos);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChangeListener(List<NoteEditModel> models);
    }

    public NoteEditAdapter(Context context, List<NoteEditModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public NoteEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note_edit, parent, false);
        NoteEditViewHolder viewHolder
                = new NoteEditViewHolder(v, new FormatTextChangeWatcher());
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
        if (onKeyDownFinishListener != null) {                        //设置按键事件完成回调
            holder.setOnKeyDownFinishListener(onKeyDownFinishListener);
        }


        final NoteEditModel itemDate = mDataList.get(position);             //获取dateBean

        //判断我们加载的到底是什么类型的数据
        if (itemDate.getItemFlag() == NoteEditModel.Flag.TEXT) {     //如果是文本Item
            /*
            * 文本
            * */
            //显示内容

            //因为HTML转Span之后块级元素会自动换行，所以这里直接手动将块级元素P删除掉，有点暴力
            //通过将<p>元素替换为<span>元素来避免以为块级元素自动添加换号引起的麻烦
            String contentHtml = mDataList.get(position).getContent();

            contentHtml = contentHtml.replace("\n", "").replace("/p><p", "/span><br><span").replace("<p", "<span").replace("/p>", "/span>");

            holder.removeTextWatcher();
            if (Build.VERSION.SDK_INT >= N) {
                holder.etItem.setText(Html.fromHtml(contentHtml, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.etItem.setText(Html.fromHtml(contentHtml));
            }
            holder.addTextWatcher();


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
                    if (onItemEditHasFocusListener != null && hasFocus) {
                        onItemEditHasFocusListener.onItemEditHasFocusListener(v, position);
                    }
                }
            });

//            //设置焦点变化监听
//            if (onSelectionChangeListener != null) {
//                holder.etItem.setOnSelectionChangeListener(new SelectionEditText.OnSelectionChangeListener() {
//                    @Override
//                    public void onSelectionChangeListener(int selStart, int selEnd) {
//                        onSelectionChangeListener.onSelectionChangeListener(holder.etItem.getText(),selStart,selEnd);
//                    }
//                });
//            }

            /**
             * 设置正则识别点击事件
             */
            if (onRegularClickListener != null) {
                holder.etItem.setRegularClickListener(onRegularClickListener);
            }


            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
            holder.llEditContainer.setVisibility(View.VISIBLE);          //文本显示 图片隐藏
            holder.rlItemImg.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(GONE);
            holder.tvItemTimeRecord.setVisibility(GONE);

            //设置引用格式
            holder.setFormat_quote(itemDate.isFormat_quote());
            //设置列表格式
            holder.setFormat_list(itemDate.isFormat_list());
            //设置勾选框样式
            holder.setForamtCheckBox(itemDate.isFormat_show_checkbox(), itemDate.isForamt_checkBox_check());
            //设置标题文字
            holder.setFormat_title(itemDate.isFormat_title());
            //设置对齐方式
            if (!itemDate.isFormat_title()) {
                holder.setFormat_align_center(itemDate.isFormat_align_center());
            }


            //设置指定的Item获取焦点
            if (focusItemPos == position) {
                holder.etItem.setFocusable(true);
                holder.etItem.setFocusableInTouchMode(true);
                holder.etItem.requestFocus();
                holder.etItem.setSelection(holder.etItem.length());
            }

            if (onSelectionChangeListener != null) {
                holder.etItem.setOnSelectionChangeListener(new SelectionEditText.OnSelectionChangeListener() {
                    @Override
                    public void onSelectionChangeListener(int selStart, int selEnd) {
                        onSelectionChangeListener.onSelectionChangeListener(holder.etItem.getText(), selStart, selEnd);
                    }
                });
            }

        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.IMAGE) {//如果是图片Item
            /*
            * 图片
            * */
            holder.rlItemImg.setVisibility(View.VISIBLE);
            holder.llEditContainer.setVisibility(GONE);
            holder.ivTextQuote.setVisibility(GONE);
            holder.ivTextPonit.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(GONE);
            holder.ivNoteImageChecked.setVisibility(GONE);
            holder.tvItemTimeRecord.setVisibility(GONE);

            if (itemDate.getImagePath() == null) {
                return;
            }

            /*
            * 根据图片的宽高来设置相框的大小
            * */
            int imgWidth = ImageUtils.getBitmapWidth(itemDate.getImagePath(), true);
            int imgHeight = ImageUtils.getBitmapWidth(itemDate.getImagePath(), false);

            Log.i(TAG, "图片宽: " + imgWidth + ",图片高: " + imgHeight);

            int editWidth = MyApplication.getEditScopeWidth();

            ViewGroup.LayoutParams layoutParams = holder.ivItemImage.getLayoutParams();
            if (imgHeight > imgWidth) {
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.item_edit_image_width_ver);
                layoutParams.width = editWidth;
                layoutParams.height = layoutParams.width * imgHeight / imgWidth;
                Log.i(TAG, "相框宽: " + layoutParams.width + ",相框高: " + layoutParams.height);

            } else {
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.item_edit_image_width_hor);
                layoutParams.width = editWidth;
                layoutParams.height = layoutParams.width * imgHeight / imgWidth;
                Log.i(TAG, "相框宽: " + imgWidth + ",相框高: " + imgHeight);

            }
            holder.ivNoteImageChecked.setLayoutParams(layoutParams);
            holder.ivItemImage.setLayoutParams(layoutParams);
            holder.ivItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            /*
            * 设置图片
            * */
            Glide.with(context)
                    .load(itemDate.getImagePath())
                    .into(holder.ivItemImage);
            /*
            * 设置高斯模糊背景图
            * */
            Glide.with(context)
                    .load(itemDate.getImagePath())
                    .crossFade(1000)
                    .bitmapTransform(new BlurTransformation(context, 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                    .into(holder.ivNoteImageChecked);


            holder.ivItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListener != null) {
                        onImageItemClickListener.onImageClickListener(holder.getAdapterPosition(), v, itemDate);
                    }

                }
            });

            /*
            * 设置图片点击事件
            * */
            holder.ivItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListener != null) {
                        onImageItemClickListener.onImageClickListener(holder.getAdapterPosition(), v, itemDate);
                    }
                }
            });

            /*
            * 设置图片长按拖拽
            * */
            holder.ivItemImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //当触摸到滑动按钮的时候
                    holder.ivNoteImageChecked.setVisibility(View.VISIBLE);
                    onStartDragListener.onStartDragListener(viewHolder);
                    return true;
                }
            });

        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.SEPARATED) {
            /*
            * 分隔线
            * */
            holder.rlItemImg.setVisibility(GONE);
            holder.llEditContainer.setVisibility(GONE);
            holder.ivTextQuote.setVisibility(GONE);
            holder.ivTextPonit.setVisibility(GONE);
            holder.tvItemTimeRecord.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(View.VISIBLE);

        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.TIMERECORD) {
            /*
            * 分隔线
            * */
            holder.rlItemImg.setVisibility(GONE);
            holder.llEditContainer.setVisibility(GONE);
            holder.ivTextQuote.setVisibility(GONE);
            holder.ivTextPonit.setVisibility(GONE);
            holder.rlItemSeparated.setVisibility(View.GONE);
            holder.tvItemTimeRecord.setVisibility(View.VISIBLE);
            holder.tvItemTimeRecord.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", itemDate.getFormat_time_record()));
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
        moveToPos = toPos;
    }

    /**
     * 替换Item位置完毕
     */
    @Override
    public void onItemMoveDone() {
        /*
        * 当我们移动到最后一个Item的位置的时候就需要在后面添加一个文本Item
        * */
        if (moveToPos >= mDataList.size() - 1) {
            mDataList.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
            notifyItemInserted(moveToPos + 1);
        }
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
     *
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

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    /**
     * 设置焦点监听
     *
     * @param onSelectionChangeListener
     */
    public void setOnSelectionChangeListener(Configs.OnSelectionChangeListener onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }

    /**
     * 正则表达式特殊文本点击事件
     *
     * @return
     */
    public OnRegularClickListener getOnRegularClickListener() {
        return onRegularClickListener;
    }

    public void setOnRegularClickListener(OnRegularClickListener onRegularClickListener) {
        this.onRegularClickListener = onRegularClickListener;
    }

    /**
     * 设置文本变化事件监听
     *
     * @return
     */
    public OnEditTextChangeListener getOnEditTextChangeListener() {
        return onEditTextChangeListener;
    }

    public void setOnEditTextChangeListener(OnEditTextChangeListener onEditTextChangeListener) {
        this.onEditTextChangeListener = onEditTextChangeListener;
    }

    public boolean isHistoryRefresh() {
        return isHistoryRefresh;
    }

    public void setHistoryRefresh(boolean historyRefresh) {
        isHistoryRefresh = historyRefresh;
    }

    /**
     * ViewHolder
     */
    public class NoteEditViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_edit_container)
        public LinearLayout llEditContainer;    //edit文本区域
        @Bind(R.id.tv_item_recylcer)
        public ClickableEditText etItem;                 //编辑文本框
        @Bind(R.id.iv_text_point)
        public ImageView ivTextPonit;           //编辑栏位列表图标
        @Bind(R.id.cb_text_check)
        public CheckBox cbTextCheck;           //编辑栏位勾选框
        @Bind(R.id.iv_text_quote)
        public ImageView ivTextQuote;           //编辑栏位引用图标
        @Bind(R.id.rl_item_img)
        public RelativeLayout rlItemImg;        //Image区域容器
        @Bind(R.id.iv_item_img)
        public ImageView ivItemImage;           //图片框
        @Bind(R.id.cv_item_img)
        public CardView cvItemImg;              //图片容器
        //        @Bind(R.id.bm_item_image)
//        public BombMenu bmItemImage;            //弹射菜单
        @Bind(R.id.iv_note_image_checked)
        public ImageView ivNoteImageChecked; //弹射菜单容器
        @Bind(R.id.iv_swipe_notice)
        public View ivSwipeNotice;              //拖拽切换的时候的提示图标
        @Bind(R.id.rl_item_separated)
        public RelativeLayout rlItemSeparated;  //分割线容器
        @Bind(R.id.tv_item_time_record)
        public TextView tvItemTimeRecord;       //时间记录分割线


        private OnKeyDownFinishListener onKeyDownFinishListener;//按键按下事件监听

        public boolean format_align_center = false;  //居中
        public boolean format_bold = false;          //加粗
        public boolean format_italic = false;        //斜体
        public boolean format_list = false;          //列表
        public boolean format_quote = false;         //引用
        public boolean format_title = false;         //标题样式
        public boolean format_underlined = false;    //下划线
        public boolean format_strike_through = false;    //下划线
        public boolean foramt_show_checkBox = false;       //勾选框
        public boolean foramt_checkBox_checked = false;  //勾选框是否勾选

        /*
        * 每一个Item都需要保存对应的位置：便于管理
        * */
        private int currentPos;             //当前的position

        private NoteEditModel model;        //用来操作的模板

        /*
        * 设置与获取当前Item的位置
        * */
        public int getCurrentPos() {
            return currentPos;
        }

        public void setCurrentPos(final int currentPos) {
            this.currentPos = currentPos;
            formatTextChangeWatcher.initWatcher(etItem, currentPos, new FormatTextChangeWatcher.FormatTextChangeToDoListener() {
                @Override
                public void formatTextChangeToDoListener(CharSequence s, int position, int selectionIndex) {
                    if (android.os.Build.VERSION.SDK_INT >= N) {
                        Log.i(TAG, "onTextChanged: --->" + Html.toHtml(etItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                        mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                    } else {
                        mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText()));
                    }
                    etItem.setSelection(selectionIndex);

                    /**
                     * 文本变化监听
                     */
                    if (!isHistoryRefresh) {//如果是历史数据刷新的刷就不需要更新到历史数据中
                        if (onEditTextChangeListener != null) {
                            try {
                                List<NoteEditModel> models = ListUtils.deepCopy(mDataList);
                                //每次都记录上一次的文本内容
                                if (lastModels != null) {
                                    onEditTextChangeListener.onEditTextChangeListener(lastModels);
                                }
                                lastModels = models;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (position == (0)) {
                            isHistoryRefresh = false;
                        }
                    }
                }
            });
        }

        /*
        * 文本变化监听
        * */
        public FormatTextChangeWatcher formatTextChangeWatcher;

        public NoteEditViewHolder(View itemView, FormatTextChangeWatcher formatTextChangeWatcher) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //初始化获取焦点
            llEditContainer.setFocusable(true);
            llEditContainer.setFocusableInTouchMode(true);
            //如果item获取到了焦点，那么默认的编辑框也就获取到了焦点
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

            //设置文本变化监听
            this.formatTextChangeWatcher = formatTextChangeWatcher;

            etItem.addTextChangedListener(formatTextChangeWatcher);        //当文本发生变化的时候就保存对应的内容到dataList

            /**
             * 设置Edit按键监听：判断按键行为作出不同的处理方式
             */
            etItem.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    /*
                    * 如果按下了Enter按键
                    * */
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (isFormat_list() || isFormat_quote() || isForamt_show_checkBox() || isFormat_title()) {
                            /*
                            * 如果此时这个Item是富文本引用或者列表类型
                            * 就初始化一个文本Item，当然内容为空
                            * */
                            model = new NoteEditModel();
                            model.setItemFlag(NoteEditModel.Flag.TEXT);
                            model.setImagePath(null);
                            model.setContent("");

                            if (isFormat_list()) {
                                /*
                                * 如果富文本样式为列表，那么就需要在下方在添加一个列表
                                * */
                                if (etItem.length() > 0) {
                                    /*
                                    * 当这个列表中有内容的时候，换行下一个才会是列表格式
                                    * 否则下一个就不是列表格式
                                    * */
                                    model.setFormat_list(isFormat_list());
                                    model.setFormat_quote(!isFormat_list());
                                    model.setFormat_show_checkbox(!isFormat_list(), !isFormat_list());
                                }
                            } else if (isForamt_show_checkBox()) {
                                /*
                                * 如果富文本样式为列表，那么就需要在下方在添加一个列表
                                * */
                                if (etItem.length() > 0) {
                                    /*
                                    * 当这个checkBox中有内容的时候
                                    * 否则下一个就不是checkBox
                                    * */
                                    model.setFormat_show_checkbox(isForamt_show_checkBox(), false);
                                    model.setFormat_quote(!isForamt_show_checkBox());
                                    model.setFormat_list(!isForamt_show_checkBox());

                                }
                            }
                            /*
                            * 将数据同步到List中
                            * */
                            mDataList.add(currentPos + 1, model);
                            setFocusItemPos(currentPos + 1);
                            notifyDataSetChanged();

                            /*
                            * 触发按键监听
                            * */
                            if (onKeyDownFinishListener != null) {
                                onKeyDownFinishListener.onEnterFinishListner(currentPos + 1);
                            }
                        }
                    }
                    /*
                    * 如果按下了DEL按键
                    * */
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        /*
                        * 如果处于引用 列表 或者 勾选框富文本样式
                        * 那么从行首DEL就可以删除这个富文本样式
                        * */
                        if (etItem.getSelectionStart() == 0) {
                            if (isFormat_list()) {
                                /*
                                * 如果富文本样式为List，那么就取消其富文本样式
                                * */
                                setFormat_list(!isFormat_list());
                                return false;
                            } else if (isFormat_quote()) {
                                //如果该item富文本为quote那么就取消其状态
                                setFormat_quote(!isFormat_quote());
                                return false;
                            } else if (isForamt_show_checkBox()) {
                                setForamtCheckBox(false, false);
                                return false;
                            }
                        }

                        if (currentPos != 0 && etItem.getSelectionStart() == 0 && etItem.getSelectionEnd() == 0) {
                            // TODO: 2017/8/1 如果长按删除键会出现重复删除的bug
                            //如果是Eidt已经空了，那么继续按下DEL按钮就删除当前Item，焦点跳转到上一个Item

                                /*
                                * 如果已经删除的上衣个Item是分割线，那么也将其删除，然后焦点跳转到上一个Item
                                * */
                            if (mDataList.get(currentPos - 1).getItemFlag() == NoteEditModel.Flag.SEPARATED
                                    || mDataList.get(currentPos - 1).getItemFlag() == NoteEditModel.Flag.TIMERECORD) {
                                mDataList.remove(currentPos - 1);
                                setFocusItemPos(currentPos - 2);

                                /*
                            * 首先判断是否为空，也只有在内容为空的时候才需要进行特殊的处理
                            * */
                                if (TextUtils.isEmpty(etItem.getText())) {
                                    mDataList.remove(currentPos);
                                }
                            } else if (mDataList.get(currentPos - 1).getItemFlag() == NoteEditModel.Flag.TEXT) {
                                if (TextUtils.isEmpty(mDataList.get(currentPos - 1).getContent())) {
                                    mDataList.remove(currentPos - 1);
                                }
                                setFocusItemPos(currentPos - 1);

                                /*
                                * 首先判断是否为空，也只有在内容为空的时候才需要进行特殊的处理
                                * */
                                if (TextUtils.isEmpty(etItem.getText())) {
                                    mDataList.remove(currentPos);
                                }
                            } else if (mDataList.get(currentPos - 1).getItemFlag() == NoteEditModel.Flag.IMAGE) {

                            }


                            notifyDataSetChanged();

                            /*
                            * 触发按键监听
                            * */
                            if (onKeyDownFinishListener != null) {
                                onKeyDownFinishListener.onDelFinishListner(currentPos - 1);
                            }
                        }

                    }
                    return false;
                }
            });


            /*
            * 设置需要在哪种情况下屏蔽EditText的回车另作处理
            * */
            etItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return isFormat_list() || isFormat_quote() || isForamt_show_checkBox() || isFormat_title();
                }
            });


            /*
            * 设置勾选框切换勾选的时候的响应
            * */
            cbTextCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDataList.get(getCurrentPos()).setForamt_checkBox_check(isChecked);
                    setWholeEditStrikethroughSpan(isChecked);
                    setFormat_strike_through(isChecked);
                }
            });

        }

        /**
         * 设置整个EditText删除线
         *
         * @param isChecked
         */
        private void setWholeEditStrikethroughSpan(boolean isChecked) {
            //获取选择区域内所有的StyleSpan
            StrikethroughSpan[] spans = etItem.getText().getSpans(0, etItem.length(), StrikethroughSpan.class);
            //清除区域内所有的UnderLineSpan
            for (int i = 0; i < spans.length; i++) {
                removeSpan(spans[i]);
            }
            //如果本身没有Span，这里需要设置
            if (isChecked) {
                setSpan(new StrikethroughSpan(), 0, etItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        /**
         * 设置Holder中的编辑栏span特效并同步保存数据样式
         *
         * @param what  Span类型
         * @param start 起始位置
         * @param end   结束为止
         * @param flags SPAN标签
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
         * @param what Span类型
         */
        public void removeSpan(Object what) {
            etItem.getText().removeSpan(what);
            if (android.os.Build.VERSION.SDK_INT >= N) {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
            } else {
                mDataList.get(currentPos).setContent(Html.toHtml(etItem.getText()));
            }
        }


        /**
         * 设置按键（Enter/Del）按键监听
         *
         * @param onKeyDownFinishListener
         */
        public void setOnKeyDownFinishListener(OnKeyDownFinishListener onKeyDownFinishListener) {
            this.onKeyDownFinishListener = onKeyDownFinishListener;
        }

        /**
         * 设置文本对齐方式
         *
         * @param isTextCenter 是否居中对齐
         */

        public void setFormat_align_center(boolean isTextCenter) {
            format_align_center = isTextCenter;
            if (isTextCenter) {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
            mDataList.get(currentPos).setFormat_align_center(format_align_center);
            removeTextWatcher();
            etItem.setText(etItem.getText());
            etItem.setSelection(etItem.length());
            addTextWatcher();
        }

        /**
         * 获取文本对齐方式
         *
         * @return
         */
        public boolean isFormat_align_center() {
            return format_align_center;
        }

        /**
         * 设置是否字体加粗
         *
         * @param format_bold
         */
        public void setFormat_blod(boolean format_bold) {
            this.format_bold = format_bold;
            formatTextChangeWatcher.setFormat_blod(format_bold);
        }


        /**
         * 获取字体是否加粗
         *
         * @return
         */
        public boolean isFormat_bold() {
            return format_bold;
        }

        /**
         * 设置是否字体倾斜
         *
         * @param format_italic
         */
        public void setFormat_italic(boolean format_italic) {
            this.format_italic = format_italic;
            formatTextChangeWatcher.setFormat_italic(format_italic);
        }

        /**
         * 获取字体是否倾斜
         *
         * @return
         */
        public boolean isFormat_italic() {
            return format_italic;
        }

        /**
         * 设置字体下划线
         *
         * @param format_underlined
         */
        public void setFormat_underlined(boolean format_underlined) {
            this.format_underlined = format_underlined;
            formatTextChangeWatcher.setFormat_underlined(format_underlined);
        }

        /**
         * 获取字体是否下划线
         *
         * @return
         */
        public boolean isFormat_underlined() {
            return format_underlined;
        }

        /**
         * 设置字体删除线
         *
         * @param format_strike_through
         */
        public void setFormat_strike_through(boolean format_strike_through) {
            this.format_strike_through = format_strike_through;
            formatTextChangeWatcher.setFormat_strike_through(format_strike_through);
        }

        /**
         * 获取字体是否删除线
         *
         * @return
         */
        public boolean isFormat_strike_through() {
            return format_strike_through;
        }


        /**
         * 是否是标题样式
         *
         * @return
         */
        public boolean isFormat_title() {
            return format_title;
        }

        /**
         * 设置标题样式
         *
         * @param format_title
         */
        public void setFormat_title(boolean format_title) {
            this.format_title = format_title;
            if (format_title) {
                setFormat_quote(!format_title);
            }
            if (format_title) {
                setForamtCheckBox(!format_title, !format_title);
            }
            if (format_title) {
                setFormat_list(!format_title);
            }
            if (format_title) {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                etItem.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }

            //同步到数据列表
            mDataList.get(currentPos).setFormat_title(format_title);
            etItem.setTextSize(format_title ? 25 : 18);
            if (format_title) {
                setFormat_blod(false);
                setFormat_italic(false);
                setFormat_underlined(false);
                setFormat_strike_through(false);
                etItem.setText(etItem.getText());
//                etItem.setTypeface(format_title ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            }
        }

        /**
         * 设置富文本列表样式
         *
         * @param format_list
         */
        public void setFormat_list(boolean format_list) {
            this.format_list = format_list;
            /*
            * 如果此时Item为引用样式，那么强行转换为列表样式
            * */
            if (format_list) {
                setFormat_quote(!format_list);
            }
            if (format_list) {
                setForamtCheckBox(!format_list, !format_list);
            }
            if (format_list) {
                setFormat_title(!format_list);
            }
            //同步到数据列表
            mDataList.get(currentPos).setFormat_list(format_list);
            ivTextPonit.setVisibility(format_list ? View.VISIBLE : GONE);
        }

        /**
         * 是否为列表样式
         *
         * @return
         */
        public boolean isFormat_list() {
            return format_list;
        }

        /**
         * 设置富文本引用样式
         *
         * @param format_quote
         */
        public void setFormat_quote(boolean format_quote) {
            this.format_quote = format_quote;
            /*
            * 将列表强制转换为引用
            * */
            if (format_quote) {
                setFormat_list(!format_quote);
            }
            if (format_quote) {
                setForamtCheckBox(!format_quote, !format_quote);
            }
            if (format_quote) {
                setFormat_title(!format_quote);
            }
            //同步数据列表
            mDataList.get(currentPos).setFormat_quote(format_quote);
            ivTextQuote.setVisibility(format_quote ? View.VISIBLE : GONE);
            etItem.setTextColor(format_quote ? ContextCompat.getColor(context, R.color.colorGray)
                    : ContextCompat.getColor(context, R.color.colorBlackBody));
        }


        /**
         * 是否为勾选框样式
         *
         * @return
         */
        public boolean isForamt_show_checkBox() {
            return foramt_show_checkBox;
        }

        /**
         * 设置勾选框
         *
         * @param foramt_show_checkBox
         * @param foramt_checkBox_checked
         */
        public void setForamtCheckBox(boolean foramt_show_checkBox, boolean foramt_checkBox_checked) {
            this.foramt_show_checkBox = foramt_show_checkBox;
            this.foramt_checkBox_checked = foramt_checkBox_checked;
            /*
            * 如果此时Item为引用样式，那么强行转换为列表样式
            * */
            if (foramt_show_checkBox) {
                setFormat_quote(!foramt_show_checkBox);
            }
            if (foramt_show_checkBox) {
                setFormat_list(!foramt_show_checkBox);
            }
            if (foramt_show_checkBox) {
                setFormat_title(!foramt_show_checkBox);
            }
            //同步到数据列表
            mDataList.get(currentPos).setFormat_show_checkbox(foramt_show_checkBox, foramt_checkBox_checked);
            cbTextCheck.setVisibility(foramt_show_checkBox ? View.VISIBLE : GONE);
            cbTextCheck.setChecked(foramt_checkBox_checked);
            if (foramt_show_checkBox) {
                setWholeEditStrikethroughSpan(foramt_checkBox_checked);
            }
        }

        /**
         * 是否为引用样式
         *
         * @return
         */
        public boolean isFormat_quote() {
            return format_quote;
        }

        /**
         * 移除文本变化监听
         */
        public void removeTextWatcher() {
            etItem.removeTextChangedListener(formatTextChangeWatcher);
        }

        /**
         * 新增文本变化监听
         */
        public void addTextWatcher() {
            etItem.addTextChangedListener(formatTextChangeWatcher);
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
