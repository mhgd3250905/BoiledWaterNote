package com.skkk.boiledwaternote.Presenters.NoteList;

import com.skkk.boiledwaternote.Modles.DBUtils;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.Presenters.BasePersenter;
import com.skkk.boiledwaternote.Views.Home.NoteListImpl;

import java.util.List;

/*
* 
* 描    述：用于NoteList界面的管理
* 作    者：ksheng
* 时    间：2017/5/28$ 19:02$.
*/
public class NoteListPresenter extends BasePersenter<NoteListImpl> implements NoteListable{
    private NoteListImpl noteListImpl;
    private NoteModle noteModle=new NoteModle(MyApplication.getInstance().getApplicationContext());

    public NoteListPresenter(NoteListImpl noteListImpl) {
        this.noteListImpl = noteListImpl;
    }

    /**
     * 展示数据
     */
    @Override
    public void fectch() {
        List<Note> noteList = noteModle.queryAll();
        if (noteList!=null){
            noteListImpl.showList(noteList);
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
    public void showNoteList() {
        List<Note> noteList = noteModle.queryAll();
        noteListImpl.showList(noteList);
    }

    /**
     * 删除笔记
     * @param note
     * @return
     */
    @Override
    public boolean deleteNote(Note note) {
        boolean done = false;
        try {
            DaoSession session = DBUtils.getInstance(MyApplication.getInstance().getApplicationContext()).getSession();
            NoteDao noteDao = session.getNoteDao();
            noteDao.delete(note);
            done = true;
        } catch (Exception e) {
            done = false;
        }
        return done;
    }

}
