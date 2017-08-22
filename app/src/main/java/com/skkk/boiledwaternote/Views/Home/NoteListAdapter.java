package com.skkk.boiledwaternote.Views.Home;

/**
 * Created by admin on 2017/5/28.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 20:39$.
*/

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skkk.boiledwaternote.CostomViews.DragItemView.DragItemCircleView;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MyDragItemView;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * RecyclerView数据适配器
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private Context context;
    private List<Note> dataList;
    private OnItemClickListener onItemClickListener;
    private boolean isItemResetAnim = false;
    private boolean itemClickable = true;

    interface OnDragItemStatusChange {
        void onDragingListener(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);

        void onDragClose(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);
    }


    public interface OnItemClickListener {
        void onItemClickListener(View view, int pos);

        void onItemDeleteClickListener(View view, int pos);

        void onItemLockClickListener(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NoteListAdapter(Context context, List<Note> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_list, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //显示距离此刻模式的时间显示方式
        CharSequence relativeDateTimeString = DateUtils
                .getRelativeDateTimeString(context, dataList.get(position).getCreateTime().getTime(),
                        DateUtils.MINUTE_IN_MILLIS, DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME);
        holder.tvNoteListTime.setText(relativeDateTimeString);

        //获取第一段文字作为标题
        String content = dataList.get(position).getContent();
        String title = null;       //显示标题
        String imagePath = null;   //图片路径

        if (!TextUtils.isEmpty(content)) {
            NoteEditModel[] noteEditModels = new Gson().fromJson(content, NoteEditModel[].class);
            //获取并设置第一个Text
            for (int i = 0; i < noteEditModels.length; i++) {
                if (noteEditModels[i].getItemFlag() == NoteEditModel.Flag.TEXT) {
                    title = noteEditModels[i].getContent();
                    break;
                }
            }
            if (!TextUtils.isEmpty(title)) {
                holder.tvNoteListTitle.setText(Html.fromHtml(title).toString());
            } else {
                holder.tvNoteListTitle.setText("无题");
            }

            //获取并设置第一个图片
            for (int i = 0; i < noteEditModels.length; i++) {
                if (noteEditModels[i].getItemFlag() == NoteEditModel.Flag.IMAGE) {
                    imagePath = noteEditModels[i].getImagePath();
                    break;
                }
            }
            if (!TextUtils.isEmpty(imagePath)) {
                holder.cvNoteListImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imagePath)
                        .into(holder.ivNoteListImage);
            } else {
                holder.cvNoteListImage.setVisibility(View.GONE);
            }
        }


        if (onItemClickListener != null) {
            holder.llShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickable) {
                        int pos = holder.getAdapterPosition();
                        onItemClickListener.onItemClickListener(v, pos);
                    }
                }
            });

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    onItemClickListener.onItemDeleteClickListener(v, pos);
                }
            });

            holder.ivLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    onItemClickListener.onItemLockClickListener(v, pos);
                }
            });
        }


        /**
         * 设置Item菜单
         */
        //设置当前位置
        holder.divItem.setPosition(position);

        Log.i(TAG, "onBindViewHolder: 处理第" + position + "个Item的菜单状态");
        if (dataList.get(position).isMenuOpen()) {
            Log.i(TAG, "onBindViewHolder: 正在关闭第" + position + "个Item");
            holder.divItem.closeItem();
            Note note = dataList.get(position);
            note.setMenuOpen(false);
            dataList.set(position, note);
        } else {
            holder.divItem.resetItem();
            Note note = dataList.get(position);
            note.setMenuOpen(false);
            dataList.set(position, note);
        }

        /**
         * 设置拖拽状态监听事件
         */
        holder.divItem.setOnItemDragStatusChange(new MyDragItemView.OnItemDragStatusChange() {
            @Override
            public void onItemDragStatusOpen(int position) {
                itemClickable = false;
            }

            @Override
            public void onItemDragStatusClose(int position) {
                itemClickable = true;
            }

            @Override
            public void onItemMenuStatusOpen(int position) {
                Log.i(TAG, "onItemDragStatusOpen: 响应Item拖拽开启" + position);
                dataList.get(position).setMenuOpen(true);
                for (int i = 0; i < dataList.size(); i++) {
                    Log.i(TAG, position+"->" + dataList.get(i).isMenuOpen());
                }
            }

            @Override
            public void onItemMenuStatusClose(int position) {
                Log.i(TAG, "onItemDragStatusOpen: 响应Item拖拽guanb" + position);
                dataList.get(position).setMenuOpen(false);
                for (int i = 0; i < dataList.size(); i++) {
                    Log.i(TAG, position+"->" + dataList.get(i).isMenuOpen());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 数据操作
     *
     * @return
     */
    public List<Note> getDataList() {
        return dataList;
    }

    public void setDataList(List<Note> dataList) {
        this.dataList = dataList;
    }

    /**
     * 是否需要使用动画的方式重置所有的Item
     *
     * @return
     */
    public boolean isItemResetAnim() {
        return isItemResetAnim;
    }

    public void setItemResetAnim(boolean itemResetAnim) {
        isItemResetAnim = itemResetAnim;
    }

    public boolean isHaveItemMenuOpen() {
        for (Note note : dataList) {
            if (note.isMenuOpen()) {
                return true;
            }
        }
        return false;
    }


//    public void setHaveItemOpen(boolean haveItemOpen) {
//        this.haveItemOpen = haveItemOpen;
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_note_list_title)
        public TextView tvNoteListTitle;
        @Bind(R.id.tv_note_list_time)
        public TextView tvNoteListTime;
        @Bind(R.id.ll_show)
        public CardView llShow;
        @Bind(R.id.ll_hide)
        public CardView llHide;
        @Bind(R.id.iv_delete)
        public ImageView ivDelete;
        @Bind(R.id.iv_lock)
        public ImageView ivLock;
        @Bind(R.id.div_item)
        public MyDragItemView divItem;
        @Bind(R.id.iv_note_list_image)
        public ImageView ivNoteListImage;
        @Bind(R.id.cv_note_list_image)
        public CardView cvNoteListImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
