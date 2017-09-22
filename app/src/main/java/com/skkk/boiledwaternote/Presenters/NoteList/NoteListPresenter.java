package com.skkk.boiledwaternote.Presenters.NoteList;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.Presenters.BasePersenter;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.Home.NoteListImpl;

import java.util.ArrayList;
import java.util.List;

/*
* 
* 描    述：用于NoteList界面的管理
* 作    者：ksheng
* 时    间：2017/5/28$ 19:02$.
*/
public class NoteListPresenter extends BasePersenter<NoteListImpl> implements NoteListable {
    //    private NoteListImpl noteListImpl;
    private NoteModle noteModle = new NoteModle(MyApplication.getInstance().getApplicationContext());
    private List<Note> mDataList;


//    public NoteListPresenter(NoteListImpl noteListImpl) {
//        this.noteListImpl = noteListImpl;
//    }

    public NoteListPresenter() {
        mDataList = new ArrayList<>(); //初始化数据集
    }

    /**
     * 显示指定类型的笔记
     *
     * @param noteType
     */
    @Override
    public void showNotes(int noteType) {
        mDataList = noteModle.query(noteType);
        if (mDataList != null) {
            getView().showList(mDataList);
        }
    }


    /**
     * 显示所有的笔记
     */
    @Override
    public void showAllNote() {
        List<Note> noteList = noteModle.query(Note.NoteType.ALL_NOTE.getValue());
        getView().showList(noteList);
    }


    public void insertLatestNote(int noteType) {
        List<Note> queryList = noteModle.query(noteType);
        if (queryList.size() > 0) {
            mDataList.add(0, queryList.get(0));
            getView().insertNote(0);
        }

    }

    /**
     * 删除笔记
     *
     * @param pos
     * @return
     */
    @Override
    public void deleteNote(int pos) {
        Note note = mDataList.get(pos);
        boolean done = noteModle.deleteOne(note);
        if (done) {
            mDataList.remove(pos);
            getView().resetAdapterData(mDataList);
            getView().deleteNote(pos);
        } else {
            getView().showNotice(R.string.note_list_article_item_delete_failed);
        }
    }

    /**
     * 更新笔记到隐私
     *
     * @param pos
     * @return
     */
    @Override
    public void updateNoteToPrivacy(int pos) {
        Note note = mDataList.get(pos);
        note.setNoteType(Note.NoteType.PRIVACY_NOTE.getValue());
        boolean done = noteModle.updateOne(note);
        if (done) {
            getView().resetAdapterData(mDataList);
            mDataList.remove(pos);
            getView().resetAdapterData(mDataList);
            getView().deleteNote(pos);
        } else {
            getView().showNotice(R.string.note_list_save_privacy_failed);
        }
    }

    /**
     * 更新笔记到隐私
     *
     * @param pos
     * @return
     */
    @Override
    public void updateNoteFromPrivacy(int pos) {
        Note note = mDataList.get(pos);
        note.setNoteType(Note.NoteType.ARTICLE_NOTE.getValue());
        boolean done = noteModle.updateOne(note);
        if (done) {
            getView().resetAdapterData(mDataList);
            mDataList.remove(pos);
            getView().resetAdapterData(mDataList);
            getView().deleteNote(pos);
        } else {
            getView().showNotice(R.string.note_list_save_privacy_failed);
        }
    }

    /**
     * 获取指定位置的Note
     *
     * @param pos
     * @return
     */
    @Override
    public Note getNote(int pos) {
        return mDataList.get(pos);
    }

    /**
     * 保存笔记,并且更新界面
     */
    @Override
    public void saveNote(String noteContent) {
        NoteEditModel noteEditModel = new NoteEditModel(noteContent, NoteEditModel.Flag.TEXT, null);
        if (noteModle.saveOne(Note.NoteType.NOTE_NOTE.getValue(), true, noteEditModel)) {
            insertLatestNote(Note.NoteType.NOTE_NOTE.getValue());
            getView().clearNoteEditText();
        } else {
            getView().showNotice(R.string.note_save_failed);
        }
    }

    /**
     * 跳转的笔记编辑界面
     *
     * @param pos
     */
    @Override
    public void startEditActivity(int pos) {
        getView().startActivity(getNote(pos));
    }

    /**
     * 显示指定类型的笔记
     *
     * @param noteType
     */
    @Override
    public void showSpecialTypeNotes(int noteType) {
        List<Note> articles = noteModle.query(noteType);
        getView().showList(articles);
    }

    @Override
    public void showAllImages() {

    }


}
