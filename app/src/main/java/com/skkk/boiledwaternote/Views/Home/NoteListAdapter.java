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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.DragItemView.DragItemCircleView;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MyDragItemView;
import com.skkk.boiledwaternote.CostomViews.MLayoutManager.LayoutManagerScrollImpl;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private int layoutStyle = 0;
    private LayoutManagerScrollImpl layoutManager;
    private boolean isMenuOpen=true;

    interface OnDragItemStatusChange {
        void onDragingListener(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);
        void onDragClose(int pos, DragItemCircleView item, View changedView, int left, int top, int dx, int dy);
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int pos);

        void onItemDeleteClickListener(View view, int pos);

        void onItemLockClickListener(View view, int pos);

        void onItemUnlockClickListener(View view, int pos);

        void onItemRecycleClickListener(View view, int pos);

        void onNoteItemLongClickListener(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NoteListAdapter(Context context, List<Note> dataList, int noteType, int layoutStyle) {
        this.context = context;
        this.dataList = dataList;
        this.noteType = noteType;
        this.layoutStyle = layoutStyle;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = 0;
        if (layoutStyle == Configs.NOTE_LIST_LAYOUT_STYLE_LINEAR) {
            layoutResId = R.layout.item_note_list_linear;
        } else if (layoutStyle == Configs.NOTE_LIST_LAYOUT_STYLE_STAGGER) {
            layoutResId = R.layout.item_note_list_linear;
        }
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(layoutResId, parent, false));
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

            holder.divItem.setPosition(holder.getAdapterPosition());

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


                /*
                * 显示图片标记
                * */
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

             /*
            * 设置隐私界面的上锁图标
            * */
            if (dataList.get(position).getNoteType() == Note.NoteType.PRIVACY_NOTE.getValue()) {
                holder.ivLock.setImageResource(R.drawable.vector_drawable_unlock);
            } else if (dataList.get(position).getNoteType() == Note.NoteType.RECYCLE_NOTE.getValue()) {
                holder.ivLock.setImageResource(R.drawable.vector_drawable_recycle);
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
                        if (dataList.get(position).getNoteType() == Note.NoteType.PRIVACY_NOTE.getValue()) {
                            onItemClickListener.onItemUnlockClickListener(v, holder.getAdapterPosition());
                        } else if (dataList.get(position).getNoteType() == Note.NoteType.RECYCLE_NOTE.getValue()) {
                            onItemClickListener.onItemRecycleClickListener(v, holder.getAdapterPosition());
                        } else {
                            onItemClickListener.onItemLockClickListener(v, holder.getAdapterPosition());

                        }
                    }
                });

            }


            /**
             * 设置Item菜单
             */
//            if (isMenuOpen){
//                holder.divItem.closeItem();
//            } else {
//                holder.divItem.openItem();
//            }
//            /**
//             * 设置拖拽状态监听事件
//             */
//            holder.divItem.setOnItemDragStatusChange(new MyDragItemView.OnItemDragStatusChange() {
//                @Override
//                public void onItemDragStatusOpen(int pos) {
//                    Log.i(TAG, "打开方向拖拽"+pos);
//                    itemClickable = false;
//                }
//
//                @Override
//                public void onItemDragStatusClose(int pos) {
//                    Log.i(TAG, "打开方向拖拽"+pos);
//                    itemClickable = false;
//
//                }
//
//                @Override
//                public void onItemMenuStatusOpen(int pos) {
//                    Log.i(TAG, "菜单打开了" + pos);
//                    itemClickable = true;
//                    dataList.get(pos).setMenuOpen(true);
//                    layoutManager.setScroll(false);
//                }
//
//                @Override
//                public void onItemMenuStatusClose(int pos) {
//                    itemClickable = true;
//                    Log.i(TAG, "菜单关闭了" + pos);
//                    dataList.get(pos).setMenuOpen(false);
//                    for (int i = 0; i < dataList.size(); i++) {
//                        Log.i(TAG, position + "->" + dataList.get(i).isMenuOpen());
//                    }
//                    layoutManager.setScroll(true);
//                }
//            });
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
        Note note;
        boolean menuOpen=false;
        for (int i = 0; i < dataList.size(); i++) {
            note=dataList.get(i);
            if (note.isMenuOpen()) {
                notifyItemChanged(i);
                menuOpen= true;
            }
        }
        return menuOpen;
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

    /**
     * 设置布局样式
     *
     * @param layoutStyle
     */
    public void setLayoutStyle(int layoutStyle) {
        this.layoutStyle = layoutStyle;    }


    /**
     * 获取布局管理器
     * @return
     */
    public LayoutManagerScrollImpl getLayoutManager() {
        // TODO: 2017/10/15 必须要完成的事情
        //请尽快把王越蓉接走，我不想伤害她，她继续摧毁我的生活，我不确定会做出什么事情
        //请不要跟王越蓉说是我说的，我实在没办法才请求您帮忙，否则她又会无休止的地侮辱我
        //感谢您，请您尽快
        return layoutManager;
    }

    /**
     * 设置布局管理器
     * @param layoutManager
     */
    public void setLayoutManager(LayoutManagerScrollImpl layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * 切换菜单状态
     */
    public void changeMenuStatus(){
        isMenuOpen=!isMenuOpen;
        notifyDataSetChanged();
    }

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
