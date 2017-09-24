package com.skkk.boiledwaternote;

import com.skkk.boiledwaternote.Views.NoteImage.NoteImageViewHolder;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 18:06$.
*/
public class Collect {
    public interface OnImageFragmentItemClickListener{
        void onImagePreviewClickListener(NoteImageViewHolder holder);
        void onBackgroundClickListener(NoteImageViewHolder holder);
        void onImageCheckClickListener(NoteImageViewHolder holder);
        void onImageLongClickListener(NoteImageViewHolder holder);
    }
}
