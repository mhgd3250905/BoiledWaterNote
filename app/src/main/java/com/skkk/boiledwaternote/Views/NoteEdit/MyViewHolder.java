package com.skkk.boiledwaternote.Views.NoteEdit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skkk.boiledwaternote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/4/22.
 */
/*
* 
* 描    述：ViewHolder
* 作    者：ksheng
* 时    间：2017/4/22$ 22:37$.
*/
public class MyViewHolder extends RecyclerView.ViewHolder{
    @Bind(R.id.tv_item_recylcer)
    public TextView tvItem;
    @Bind(R.id.iv_item_move)
    public ImageView ivItemMove;
    @Bind(R.id.iv_item_img)
    public ImageView ivItemImage;

    public MyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
