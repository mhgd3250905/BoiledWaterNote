package com.skkk.boiledwaternote.Views.NoteImage;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.Presenters.BasePersenter;
import com.skkk.boiledwaternote.R;

import java.util.List;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/9/24$ 16:52$.
*/
public class NoteImagePresent extends BasePersenter<NoteImageImpl> implements NoteImageable{
    private NoteModle noteModle = new NoteModle(MyApplication.getInstance().getApplicationContext());
    private List<ImageModle> imageModleList;

    public NoteImagePresent() {

    }

    /**
     * 显示所有的图片
     * @param noteType
     */
    @Override
    public void showImages(int noteType){
        List<NoteEditModel> modelList = noteModle.queryAllImages(noteType);
        imageModleList = ImageModle.noteEditModle2ImageModle(modelList);
        getView().showImages(imageModleList);
    }

    /**
     * 删除一个图片
     * @param pos
     * @return
     */
    @Override
    public boolean deleteImage(int pos,int noteType) {
        if (noteModle.deleteImage(imageModleList.get(pos),noteType)) {
            imageModleList.remove(pos);
            getView().deleteImage(pos);
            return true;
        }else {
            getView().showNotice(R.string.image_list_delete_failed);
            return false;
        }
    }

    /**
     * 批量删除图片
     * @param deleteImageList
     * @return
     */
    @Override
    public boolean deleteAllImage(List<ImageModle> deleteImageList,int noteType) {
        if (noteModle.deleteAllImages(deleteImageList,noteType)) {
            imageModleList.removeAll(deleteImageList);
            showImages(Note.NoteType.ALL_NOTE.getValue());
            return true;
        }else {
            getView().showNotice(R.string.image_list_delete_failed);
            return false;
        }
    }


    /**
     * 获取一个图片
     * @return
     */
    @Override
    public NoteEditModel getImage() {
        return null;
    }

    /**
     * 跳转到图片详情
     */
    @Override
    public void startPreviewActivity(int pos) {
        ImageModle imageModle = imageModleList.get(pos);
        getView().startToPreviewActivity(imageModle);
    }

    @Override
    public void changeEditStatus(int pos,boolean curIsEdit) {

    }

    /**
     * 编辑状态下点击图片切换状态
     * @param holder
     * @param changeToFalse
     */
    @Override
    public void changeItemDeleteStatus(NoteImageViewHolder holder,boolean changeToFalse) {
        if (changeToFalse) {
            imageModleList.get(holder.getAdapterPosition()).setNeedDelete(false);
            getView().changeItemDeleteStatus(holder,false);
        }else {
            imageModleList.get(holder.getAdapterPosition()).setNeedDelete(!holder.isNeedDelete());
            getView().changeItemDeleteStatus(holder,!holder.isNeedDelete());
        }

    }
}
