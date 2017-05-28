package com.skkk.boiledwaternote.Views.NoteEdit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.skkk.boiledwaternote.R;

import java.io.File;
import java.util.Collections;
import java.util.List;

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
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<DataModel> mDataList;
    private OnStartDragListener onStartDragListener;

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    public MyAdapter(Context context, List<DataModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_edit, parent, false));
        return viewHolder;
    }

    public void setmDataList(List<DataModel> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataModel itemDate=mDataList.get(position);

        if (itemDate.getItemFlag()== DataModel.Flag.TEXT) {//如果是文本Item
            holder.tvItem.setText(mDataList.get(position).getContent());
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
            holder.tvItem.setVisibility(View.VISIBLE);
            holder.ivItemImage.setVisibility(View.GONE);
            holder.ivItemMove.setVisibility(View.GONE);

        }else if (itemDate.getItemFlag()== DataModel.Flag.IMAGE){//如果是图片Item
            holder.ivItemImage.setVisibility(View.VISIBLE);
            holder.ivItemMove.setVisibility(View.VISIBLE);
            holder.tvItem.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            layoutParams.height= 960;
            holder.itemView.setLayoutParams(layoutParams);

            holder.ivItemMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event)==MotionEvent.ACTION_DOWN
                            &&onStartDragListener!=null){
                        onStartDragListener.onStartDragListener(holder);
                    }
                    return false;
                }
            });

            if (itemDate.getImagePath()==null){
                return;
            }
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

        Log.d("MyAdapter", "holder.itemView.getLayoutParams().height:" + holder.itemView.getLayoutParams().height);
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
}
