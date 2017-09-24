package com.skkk.boiledwaternote.Modles;

import com.skkk.boiledwaternote.Views.NoteImage.ImageModle;

import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/11$ 15:24$.
*/
interface NoteModleImpl<T> {
    boolean saveOne(int type,T t);
    boolean updateOne(T t);
    boolean deleteOne(T t);
    List<T> query(int noteType);
    List<NoteEditModel> queryAllImages(int type);
    boolean deleteImage(ImageModle model,int type);
    boolean deleteAllImages(List<ImageModle> modelList,int type);
}
