package com.skkk.boiledwaternote.Views.NoteImage;

import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 16:53$.
*/
public interface NoteImageable {
    void showImages(int noteType);
    boolean deleteImage(int pos,int noteType);
    boolean deleteAllImage(List<ImageModle> deleteImages,int noteType);
    NoteEditModel getImage();
    void startPreviewActivity();
    void changeEditStatus(int pos,boolean curIsEdit);
    void changeItemDeleteStatus(NoteImageViewHolder holder,boolean changeToFalse);
}
