package com.skkk.boiledwaternote.Views.NoteImage;

import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 22:07$.
*/
public class ImageModle implements Serializable{
    private boolean needDelete;
    private NoteEditModel noteEditModel;

    public ImageModle(NoteEditModel noteEditModel, boolean needDelete) {
        this.noteEditModel = noteEditModel;
        this.needDelete = needDelete;
    }

    public NoteEditModel getnoteEditModel() {
        return noteEditModel;
    }

    public void setnoteEditModel(NoteEditModel noteEditModel) {
        this.noteEditModel = noteEditModel;
    }

    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        this.needDelete = needDelete;
    }

    public static List<ImageModle> noteEditModle2ImageModle(List<NoteEditModel> noteEditModelList){
        List<ImageModle> imageModleList=new ArrayList<>();
        for (int i = 0; i < noteEditModelList.size(); i++) {
            imageModleList.add(new ImageModle(noteEditModelList.get(i),false));
        }
        return imageModleList;
    }

}
