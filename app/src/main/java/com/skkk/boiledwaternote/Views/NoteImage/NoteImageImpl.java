package com.skkk.boiledwaternote.Views.NoteImage;

import android.support.annotation.StringRes;

import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */

public interface NoteImageImpl {
    void showImages(List<ImageModle> imageModleList);
    void startToPreviewActivity(ImageModle modle);
    void deleteImage(int pos);
    void deleteAllImage(int start, int end);
    void showNotice(@StringRes int strId);
    void changeEditStatus(boolean currentIsEdit);
    void changeItemDeleteStatus(NoteImageViewHolder holder,boolean needDelete);
}
