package com.skkk.boiledwaternote.Views.NoteEdit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.ItemTouchHelperAdapter;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

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
    private OnItemEditSelectedLintener onItemEditSelectedLintener;              //Item编辑焦点监听
    private NoteEditViewHolder currentHolder;                                   //当前ViewHolder
    private OnItemKeyDownListener onItemKeyDownListener;                        //针对ItemEdit的按键监听
    private boolean alignCenterText = false;                                    //ItemEdit文字是否居中对齐
    private int focusItemPos=-1;                                                //需要获得焦点的Item

    public void setFocusItemPos(int focusItemPos) {
        this.focusItemPos = focusItemPos;
    }

    interface OnItemKeyDownListener {
        void onItemKeyEnterListener(NoteEditViewHolder viewHolder, int pos, int keyCode, KeyEvent event);

        void onItemKeyBackListener(NoteEditViewHolder viewHolder, int pos, int keyCode, KeyEvent event);
    }

    interface OnItemEditSelectedLintener {
        void onItemEditSelectedLintener(View view, int pos, boolean hasFocus);
    }

    /**
     * 设置按键监听
     *
     * @param onItemKeyDownListener
     */
    public void setOnItemKeyDownListener(OnItemKeyDownListener onItemKeyDownListener) {
        this.onItemKeyDownListener = onItemKeyDownListener;
    }


    /**
     * 设置Item编辑器焦点获取监听
     *
     * @param onItemEditSelectedLintener
     */
    public void setOnItemEditSelectedLintener(OnItemEditSelectedLintener onItemEditSelectedLintener) {
        this.onItemEditSelectedLintener = onItemEditSelectedLintener;
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
        notifyDataSetChanged();
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
        holder.myItemTextChangeListener.updatePos(position);        //更新文本变化监听pos

        NoteEditModel itemDate = mDataList.get(position);             //获取dateBean

        if (onItemEditSelectedLintener != null) {
            holder.etItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onItemEditSelectedLintener.onItemEditSelectedLintener(v, position, hasFocus);
                }
            });
        }

        //判断我们加载的到底是什么类型的数据
        if (itemDate.getItemFlag() == NoteEditModel.Flag.TEXT) {     //如果是文本Item
            //显示内容

            //因为HTML转Span之后块级元素会自动换行，所以这里直接手动将块级元素P删除掉，有点暴力
            //通过将<p>元素替换为<span>元素来避免以为块级元素自动添加换号引起的麻烦
            String contentHtml = mDataList.get(position).getContent();
            contentHtml = contentHtml.replace("<p", "<span").replace("/p>", "/span>");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
                        currentHolder = holder;
                    }
                }
            });

            if (onItemKeyDownListener != null) {
                holder.etItem.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN) {
                            onItemKeyDownListener.onItemKeyEnterListener(holder, position, keyCode, event);
                        }
                        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                            onItemKeyDownListener.onItemKeyBackListener(holder, position, keyCode, event);

                        }
                        return false;
                    }
                });
            }

            holder.etItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&actionId==KeyEvent.ACTION_DOWN){
                        return true;
                    }

                    return false;
                }
            });


            //设置文字对齐方式
            if (alignCenterText) {
                holder.setEditItemAlignCenter(View.TEXT_ALIGNMENT_CENTER);
            } else {
                holder.setEditItemAlignCenter(View.TEXT_ALIGNMENT_TEXT_START);
            }

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
            holder.etItem.setVisibility(View.VISIBLE);          //文本显示 图片隐藏
            holder.ivItemImage.setVisibility(View.GONE);
            holder.ivItemMove.setVisibility(View.GONE);

            //设置指定的Item获取焦点
            if (focusItemPos==position) {
                holder.etItem.requestFocus();
                holder.etItem.setSelection(holder.etItem.length());
            }

        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.IMAGE) {//如果是图片Item
            holder.ivItemImage.setVisibility(View.VISIBLE);     //图片显示 文本隐藏
            holder.ivItemMove.setVisibility(View.VISIBLE);
            holder.etItem.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            layoutParams.height = 800;
            holder.itemView.setLayoutParams(layoutParams);

            //拖拽图片触摸监听
            holder.ivItemMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                            && onStartDragListener != null) {
                        onStartDragListener.onStartDragListener(holder);
                    }
                    return false;
                }
            });

            if (itemDate.getImagePath() == null) {
                return;
            }

            //压缩图片
            Bitmap bitmapFromUri = new Compressor.Builder(context)
                    .setMaxHeight(800)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .build()
                    .compressToBitmap(new File(itemDate.getImagePath()));

//            Bitmap bitmapFromUri = BitmapFactory.decodeFile(itemDate.getImagePath());
            holder.ivItemImage.setImageBitmap(bitmapFromUri);
        }

//        Log.d("NoteEditAdapter", "holder.itemView.getLayoutParams().height:" + holder.itemView.getLayoutParams().height);
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
     * 左右滑动删除Item
     *
     * @param pos
     */
    @Override
    public void onitemSwipe(int pos) {
        mDataList.remove(pos);
        notifyItemRemoved(pos);
    }


    /**
     * ViewHolder
     */
    class NoteEditViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_recylcer)
        public EditText etItem;             //编辑文本框
        @Bind(R.id.iv_item_move)
        public ImageView ivItemMove;        //拖拽移动按钮
        @Bind(R.id.iv_item_img)
        public ImageView ivItemImage;       //图片框

        public MyItemTextChangeListener myItemTextChangeListener;

        public NoteEditViewHolder(View itemView, MyItemTextChangeListener myItemTextChangeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        }

        public void setEditItemAlignCenter(int textAligment) {
            etItem.setTextAlignment(textAligment);
        }


        public void setFormat_align_flag(int format_align_flag) {
            myItemTextChangeListener.setFormat_align_flag(format_align_flag);
        }

        public void setFormat_blod(boolean format_blod) {
            myItemTextChangeListener.setFormat_blod(format_blod);
        }

        public void setFormat_italic(boolean format_italic) {
            myItemTextChangeListener.setFormat_italic(format_italic);
        }

        public void setFormat_list(boolean format_list) {
            myItemTextChangeListener.setFormat_list(format_list);
        }

        public void setFormat_list_numbered(boolean format_list_numbered) {
            myItemTextChangeListener.setFormat_list_numbered(format_list_numbered);
        }

        public void setFormat_quote(boolean format_quote) {
            myItemTextChangeListener.setFormat_quote(format_quote);
        }

        public void setFormat_size(int format_size) {
            myItemTextChangeListener.setFormat_size(format_size);
        }

        public void setFormat_underlined(boolean format_underlined) {
            myItemTextChangeListener.setFormat_underlined(format_underlined);
        }
    }


    public class MyItemTextChangeListener implements TextWatcher {
        private int position;

        private EditText currentEdit;

        private boolean flagIsAuto = false;           //设置一个flag用来避免重新设置EditText时候触发监听

        private int format_align_flag = 0;            //0-左 1-中后 2-右
        private boolean format_blod = false;          //加粗
        private boolean format_italic = false;        //斜体
        private boolean format_list = false;          //列表
        private boolean format_list_numbered = false; //数字列表
        private boolean format_quote = false;         //引用
        private int format_size = 1;                  //字体大小：0-p 1-h1 2-h2 3-h3
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
                        ss.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (format_blod) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (format_italic) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //设置下划线
                if (format_underlined) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new UnderlineSpan(), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //设置删除线
                if (format_strike_through) {
                    for (int i = start; i < start + count; i++) {
                        ss.setSpan(new StrikethroughSpan(), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                currentEdit.setText(ss);
                currentEdit.setSelection(currentEdit.length());

            } else {
                flagIsAuto = false;
            }

            NoteEditModel model = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                model = new NoteEditModel(Html.toHtml(currentEdit.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL), NoteEditModel.Flag.TEXT, null);
            } else {
                model = new NoteEditModel(Html.toHtml(currentEdit.getText()), NoteEditModel.Flag.TEXT, null);
            }

            mDataList.set(position, model);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        public void setCurrentEdit(EditText currentEdit) {
            this.currentEdit = currentEdit;
        }

        public void setFormat_align_flag(int format_align_flag) {
            this.format_align_flag = format_align_flag;
        }

        public void setFormat_blod(boolean format_blod) {
            this.format_blod = format_blod;
        }

        public void setFormat_italic(boolean format_italic) {
            this.format_italic = format_italic;
        }

        public void setFormat_list(boolean format_list) {
            this.format_list = format_list;
        }

        public void setFormat_list_numbered(boolean format_list_numbered) {
            this.format_list_numbered = format_list_numbered;
        }

        public void setFormat_quote(boolean format_quote) {
            this.format_quote = format_quote;
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

        public int getFormat_align_flag() {
            return format_align_flag;
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