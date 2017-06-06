package com.skkk.boiledwaternote.Views.NoteEdit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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
    private Context context;
    private List<NoteEditModel> mDataList;
    private OnStartDragListener onStartDragListener;
    private OnItemEditSelectedLintener onItemEditSelectedLintener;
    private NoteEditViewHolder currentHolder;

    interface OnItemEditSelectedLintener {
        void onItemEditSelectedLintener(View view, int pos,boolean hasFocus);
    }

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

        if (onItemEditSelectedLintener!=null){
            holder.etItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onItemEditSelectedLintener.onItemEditSelectedLintener(v,position,hasFocus);
                }
            });
        }

        //判断我们加载的到底是什么类型的数据
        if (itemDate.getItemFlag() == NoteEditModel.Flag.TEXT) {     //如果是文本Item
            holder.etItem.setText(mDataList.get(position).getContent());
            holder.etItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        currentHolder=holder;
                    }
                }
            });
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
            holder.etItem.setVisibility(View.VISIBLE);          //文本显示 图片隐藏
            holder.ivItemImage.setVisibility(View.GONE);
            holder.ivItemMove.setVisibility(View.GONE);

        } else if (itemDate.getItemFlag() == NoteEditModel.Flag.IMAGE) {//如果是图片Item
            holder.ivItemImage.setVisibility(View.VISIBLE);     //图片显示 文本隐藏
            holder.ivItemMove.setVisibility(View.VISIBLE);
            holder.etItem.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            layoutParams.height = 960;
            holder.itemView.setLayoutParams(layoutParams);

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
                    .setMaxWidth(540)
                    .setMaxHeight(960)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .build()
                    .compressToBitmap(new File(itemDate.getImagePath()));

//            Bitmap bitmapFromUri = BitmapFactory.decodeFile(itemDate.getImagePath());
            holder.ivItemImage.setImageBitmap(bitmapFromUri);
        }

        Log.d("NoteEditAdapter", "holder.itemView.getLayoutParams().height:" + holder.itemView.getLayoutParams().height);
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

        private MyItemTextChangeListener myItemTextChangeListener;

        public NoteEditViewHolder(View itemView, MyItemTextChangeListener myItemTextChangeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.myItemTextChangeListener = myItemTextChangeListener;
            myItemTextChangeListener.setCurrentEdit(etItem);
            etItem.addTextChangedListener(myItemTextChangeListener);        //当文本发生变化的时候就保存对应的内容到dataList
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

        private boolean need2Html=true;
        private CharSequence lastS;

        private int format_align_flag=0;            //0-左 1-中后 2-右
        private boolean format_blod=false;          //加粗
        private boolean format_italic=false;        //斜体
        private boolean format_list=false;          //列表
        private boolean format_list_numbered=false; //数字列表
        private boolean format_quote=false;         //引用
        private int format_size=1;                  //字体大小：0-p 1-h1 2-h2 3-h3
        private boolean format_underlined=false;    //下划线




        public void updatePos(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            NoteEditModel model = new NoteEditModel(s.toString(), NoteEditModel.Flag.TEXT, null);
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
    }

    /**
     * 获取当前选中的ViewHolder
     * @return
     */
    public NoteEditViewHolder getCurrentHolder() {
        return currentHolder;
    }
}
