package com.skkk.boiledwaternote.Views.NoteImage;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skkk.boiledwaternote.BaseViewHolder;
import com.skkk.boiledwaternote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 17:36$.
*/
public class NoteImageViewHolder extends BaseViewHolder {
    @Bind(R.id.iv_note_image)
    public ImageView ivNoteImage;
    @Bind(R.id.rl_note_iamge_checked_container)
    public RelativeLayout rlNoteImageCheckedContainer;

    private boolean needDelete;

    public NoteImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        rlNoteImageCheckedContainer.setVisibility(needDelete?View.VISIBLE:View.GONE);
        this.needDelete = needDelete;
    }
}
