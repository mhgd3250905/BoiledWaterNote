package com.skkk.boiledwaternote.Presenters.NoteList;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.Presenters.BasePersenter;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.Home.NoteListFragment;
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

    @Override
    public void showNotes(String noteType) {
        mDataList = noteModle.query(noteType);
        if (mDataList != null) {
            getView().showList(mDataList);
        }
    }

    @Override
    public List<Note> getNotes() {
//        DaoSession session = DBUtils.getInstance(MyApplication.getInstance().getApplicationContext()).getSession();
//        NoteDao noteDao = session.getNoteDao();
//        List<Note> list = noteDao.queryBuilder().orderDesc(NoteDao.Properties.CreateTime).list();
        return null;
    }

    @Override
    public void showAllNote() {
        List<Note> noteList = noteModle.queryAll();
        getView().showList(noteList);
    }

    public void insertLatestNote() {
        Note note = noteModle.queryLatestOne();
        mDataList.add(0, note);
        getView().insertNote(0);
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
    public void updateNoteToPrivacy(int pos, String type) {
        Note note = mDataList.get(pos);
        note.setIsPrivacy(true);//设置为隐私类型
        boolean done = noteModle.updateOne(note);
        if (done) {
            getView().resetAdapterData(mDataList);
            if (type.equals(NoteListFragment.NOTE_TYPE_NONE)
                    || type.equals(NoteListFragment.NOTE_TYPE_ARTICLE)) {
                mDataList.remove(pos);
                getView().resetAdapterData(mDataList);
                getView().deleteNote(pos);
            } else if (type.equals(NoteListFragment.NOTE_TYPE_PRIVACY)) {
                showNotes(NoteListFragment.NOTE_TYPE_PRIVACY);
            }
        } else {
            getView().showNotice(R.string.note_list_save_privacy_failed);
        }
    }

    /**
     * 获取指定位置的Note
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
        if (noteModle.saveNote(2, true, noteEditModel)) {
            insertLatestNote();
            getView().clearNoteEditText();
        } else {
            getView().showNotice(R.string.note_save_failed);
        }
    }

    /**
     * 跳转的笔记编辑界面
     * @param pos
     */
    @Override
    public void startEditActivity(int pos) {
        getView().startActivity(getNote(pos));
    }

}
