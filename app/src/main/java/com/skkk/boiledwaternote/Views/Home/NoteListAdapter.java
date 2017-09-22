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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * RecyclerView数据适配器
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private Context context;
    private List<Note> dataList;
    private OnItemClickListener onItemClickListener;
    private boolean isItemResetAnim = false;
    private boolean itemClickable = true;
    private int noteType = Note.NoteType.ALL_NOTE.getValue();

    interface OnDragItemStatusChange {
        void onDragingListener(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);

        void onDragClose(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);
    }


    public interface OnItemClickListener {
        void onItemClickListener(View view, int pos);

        void onItemDeleteClickListener(View view, int pos);

        void onItemLockClickListener(View view, int pos);

        void onItemUnlockClickListener(View view, int pos);

        void onNoteItemLongClickListener(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NoteListAdapter(Context context, List<Note> dataList, int noteType) {
        this.context = context;
        this.dataList = dataList;
        this.noteType = noteType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_list, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //获取第一段文字作为标题
        String content = dataList.get(position).getContent();
        String title = "";       //显示标题
        String contentTitle = "";  //内容标题
        String imagePath = null;   //图片路径

        if (dataList.get(position).getNoteType() == Note.NoteType.NOTE_NOTE.getValue()) {
            /*
            * 如果是笔记类型
            * */
            holder.divItem.setVisibility(GONE);
            holder.rlListNoteContainer.setVisibility(VISIBLE);

            //显示距离此刻模式的时间显示方式
            CharSequence relativeDateTimeString = DateUtils
                    .getRelativeDateTimeString(context, dataList.get(position).getCreateTime().getTime(),
                            DateUtils.MINUTE_IN_MILLIS, DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME);
            holder.tvNoteListTime.setText(relativeDateTimeString);

            NoteEditModel[] noteEditModels = new Gson().fromJson(dataList.get(position).getContent(), NoteEditModel[].class);
            //获取并设置第一个Text
            for (int i = 0; i < noteEditModels.length; i++) {
                if (noteEditModels[i].getItemFlag() == NoteEditModel.Flag.TEXT) {
                    if (contentTitle.isEmpty()) {
                        contentTitle = noteEditModels[i].getContent();
                    }
                    if (noteEditModels[i].isFormat_title() && TextUtils.isEmpty(title)) {
                        title = noteEditModels[i].getContent();
                    }
                    if (!title.isEmpty()) {
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(title) || !contentTitle.isEmpty()) {
                holder.tvNoteListTitle.setText(Html.fromHtml(title.isEmpty() ? contentTitle : title).toString());
            } else {
                holder.tvNoteListTitle.setText(R.string.note_list_empty_title);
            }

            /*
            * 设置点击长按事件
            * */
            holder.rlListNoteContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onNoteItemLongClickListener(v, holder.getAdapterPosition());
                    return false;
                }
            });


        } else {
            /*
            * 如果是文章类型
            * */
            holder.divItem.setVisibility(VISIBLE);
            holder.rlListNoteContainer.setVisibility(GONE);

            //显示距离此刻模式的时间显示方式
            CharSequence relativeDateTimeString = DateUtils
                    .getRelativeDateTimeString(context, dataList.get(position).getCreateTime().getTime(),
                            DateUtils.MINUTE_IN_MILLIS, DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME);
            holder.tvArticleListTime.setText(relativeDateTimeString);

            if (!TextUtils.isEmpty(content)) {
                NoteEditModel[] noteEditModels = new Gson().fromJson(content, NoteEditModel[].class);
                //获取并设置第一个Text
                //获取并设置第一个Text
                for (int i = 0; i < noteEditModels.length; i++) {
                    if (noteEditModels[i].getItemFlag() == NoteEditModel.Flag.TEXT) {
                        if (contentTitle.isEmpty()) {
                            contentTitle = noteEditModels[i].getContent();
                        }
                        if (noteEditModels[i].isFormat_title() && TextUtils.isEmpty(title)) {
                            title = noteEditModels[i].getContent();
                        }
                        if (!title.isEmpty()) {
                            break;
                        }
                    }
                }
                if (!TextUtils.isEmpty(title) || !contentTitle.isEmpty()) {
                    holder.tvArticleListTitle.setText(Html.fromHtml(title.isEmpty() ? contentTitle : title).toString());
                } else {
                    holder.tvArticleListTitle.setText(R.string.note_list_empty_title);
                }
                //获取并设置第一个图片
                for (int i = 0; i < noteEditModels.length; i++) {
                    if (noteEditModels[i].getItemFlag() == NoteEditModel.Flag.IMAGE) {
                        imagePath = noteEditModels[i].getImagePath();
                        break;
                    }
                }


                if (!TextUtils.isEmpty(imagePath)) {
//                    /*
//                     * 根据图片的宽高来设置相框的大小
//                     * */
//                    int imgWidth = ImageUtils.getBitmapWidth(imagePath, true);
//                    int imgHeight = ImageUtils.getBitmapWidth(imagePath, false);
//                    // w:h=mW:mH
//
//                    ViewGroup.LayoutParams layoutParams = holder.ivArticleListImage.getLayoutParams();
//                    if (imgHeight > imgWidth) {
//                        //竖直图片
//                        layoutParams.height = (int) context.getResources().getDimension(R.dimen.note_list_item_height);
//                        layoutParams.width = (int) (context.getResources().getDimension(R.dimen.note_list_item_height) * imgWidth/imgHeight);
//                    } else {
//                        //横向图片
//                        layoutParams.height = (int) context.getResources().getDimension(R.dimen.note_list_item_height);
//                        layoutParams.width = (int) (context.getResources().getDimension(R.dimen.note_list_item_height) * imgWidth/imgHeight);
//                    }
//                    holder.ivArticleListImage.setLayoutParams(layoutParams);
//
//                    holder.ivArticleListImage.setVisibility(View.VISIBLE);
//                    Glide.with(context)
//                            .load(imagePath)
//                            .into(holder.ivArticleListImage);
                    holder.ivArticleListImageFlag.setVisibility(VISIBLE);
                } else {
                    holder.ivArticleListImageFlag.setVisibility(GONE);
                }
            }


//            holder.ivLock.setVisibility(noteType == Note.NoteType.PRIVACY_NOTE.getValue() ? View.GONE : View.VISIBLE);
//            holder.ivUnlock.setVisibility(noteType == Note.NoteType.PRIVACY_NOTE.getValue() ? View.VISIBLE : View.GONE);


             /*
            * 设置隐私界面的上锁图标
            * */
            if (dataList.get(position).getNoteType() == Note.NoteType.PRIVACY_NOTE.getValue()) {
                holder.ivLock.setImageResource(R.drawable.vector_drawable_unlock);
            } else {
                holder.ivLock.setImageResource(R.drawable.vector_drawable_lock);

            }


            if (onItemClickListener != null) {
                holder.llShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickable) {
                            onItemClickListener.onItemClickListener(v, holder.getAdapterPosition());
                        }
                    }
                });

                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemDeleteClickListener(v, holder.getAdapterPosition());
                    }
                });

                holder.ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataList.get(position).getNoteType()== Note.NoteType.PRIVACY_NOTE.getValue()){
                            onItemClickListener.onItemUnlockClickListener(v, holder.getAdapterPosition());

                        }else {
                            onItemClickListener.onItemLockClickListener(v, holder.getAdapterPosition());

                        }
                    }
                });

//                holder.ivUnlock.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                });
            }


            /**
             * 设置Item菜单
             */

            Log.i(TAG, "onBindViewHolder: 处理第" + position + "个Item的菜单状态");
            if (dataList.get(position).isMenuOpen()) {
                Log.i(TAG, "onBindViewHolder: 正在关闭第" + position + "个Item");
                holder.divItem.closeItem();
            } else {
                holder.divItem.resetItem();
            }

            /**
             * 设置拖拽状态监听事件
             */
            holder.divItem.setOnItemDragStatusChange(new MyDragItemView.OnItemDragStatusChange() {
                @Override
                public void onItemDragStatusOpen() {
                    itemClickable = false;
                }

                @Override
                public void onItemDragStatusClose() {
                    itemClickable = true;
                }

                @Override
                public void onItemMenuStatusOpen() {
                    Log.i(TAG, "onItemDragStatusOpen: 响应Item拖拽开启" + position);
                    dataList.get(holder.getAdapterPosition()).setMenuOpen(true);
                }

                @Override
                public void onItemMenuStatusClose() {
                    Log.i(TAG, "onItemDragStatusOpen: 响应Item拖拽guanb" + position);
                    dataList.get(holder.getAdapterPosition()).setMenuOpen(false);
                    for (int i = 0; i < dataList.size(); i++) {
                        Log.i(TAG, position + "->" + dataList.get(i).isMenuOpen());
                    }

                }
            });
        }
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

    public void resetMenuStatus() {
        notifyDataSetChanged();
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }


//    public void setHaveItemOpen(boolean haveItemOpen) {
//        this.haveItemOpen = haveItemOpen;
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_article_list_title)
        public TextView tvArticleListTitle;
        @Bind(R.id.tv_article_list_time)
        public TextView tvArticleListTime;
        @Bind(R.id.ll_show)
        public CardView llShow;
        @Bind(R.id.ll_hide)
        public LinearLayout llHide;
        @Bind(R.id.iv_delete)
        public ImageView ivDelete;
        @Bind(R.id.iv_lock)
        public ImageView ivLock;
//        @Bind(R.id.iv_unlock)
//        public ImageView ivUnlock;
        @Bind(R.id.div_item)
        public MyDragItemView divItem;
        @Bind(R.id.iv_article_list_image)
        public ImageView ivArticleListImage;
        //        @Bind(R.id.cv_article_list_image)
//        public CardView cvArticleListImage;
        @Bind(R.id.rl_note_list_container)
        public RelativeLayout rlListNoteContainer;
        @Bind(R.id.tv_note_list_title)
        public TextView tvNoteListTitle;
        @Bind(R.id.tv_note_list_time)
        public TextView tvNoteListTime;
        @Bind(R.id.iv_article_list_image_flag)
        public ImageView ivArticleListImageFlag;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
