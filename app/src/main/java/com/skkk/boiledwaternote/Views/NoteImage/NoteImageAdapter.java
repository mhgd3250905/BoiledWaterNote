package com.skkk.boiledwaternote.Views.NoteImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.skkk.boiledwaternote.BaseAdapter;
import com.skkk.boiledwaternote.Collect;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;

import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：图片适配器
* 作    者：ksheng
* 时    间：2017/9/24$ 17:12$.
*/
public class NoteImageAdapter extends BaseAdapter<ImageModle,NoteImageViewHolder>{
    private Collect.OnImageFragmentItemClickListener imageClickListener;//Item点击事件
    private boolean isEdit;//是否处于编辑状态

    public NoteImageAdapter(Context context, List<ImageModle> mDataList) {
        super(context, mDataList);
    }

    @Override
    protected NoteImageViewHolder getCostumViewHolder(ViewGroup parent, int viewType) {
        return new NoteImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_image,parent,false));
    }

    @Override
    protected void setViewHolder(final NoteImageViewHolder holder, int position) {
        ImageModle imageModle = mDataList.get(position);
        NoteEditModel noteEditModle=imageModle.getnoteEditModel();
        if (noteEditModle.getItemFlag()== NoteEditModel.Flag.IMAGE) {
            Glide.with(context)
                    .load(noteEditModle.getImagePath())
                    .into(holder.ivNoteImage);
        }

        holder.setNeedDelete(imageModle.isNeedDelete());

        if (imageClickListener!=null){
            /*
            * 图片点击事件
            * */
            holder.ivNoteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdit()) {
                        //编辑状态下点击事件
                        imageClickListener.onImageCheckClickListener(holder);
                    }else {
                        //正常状态下点击是事件
                        imageClickListener.onImagePreviewClickListener(holder);
                    }
                }
            });

            holder.ivNoteImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //正常状态下长按事件
                    imageClickListener.onImageLongClickListener(holder);
                    return true;
                }
            });

            holder.rlNoteImageCheckedContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isNeedDelete()){
                        //编辑状态下点击背景
                        imageClickListener.onBackgroundClickListener(holder);
                    }
                }
            });
        }
    }


    public void setImageClickListener(Collect.OnImageFragmentItemClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
