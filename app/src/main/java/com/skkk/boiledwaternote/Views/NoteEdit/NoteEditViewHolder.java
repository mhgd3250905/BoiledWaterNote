package com.skkk.boiledwaternote.Views.NoteEdit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
public class NoteEditViewHolder extends RecyclerView.ViewHolder{
    @Bind(R.id.tv_item_recylcer)
    public EditText etItem;
    @Bind(R.id.iv_item_move)
    public ImageView ivItemMove;
    @Bind(R.id.iv_item_img)
    public ImageView ivItemImage;

    public NoteEditViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
