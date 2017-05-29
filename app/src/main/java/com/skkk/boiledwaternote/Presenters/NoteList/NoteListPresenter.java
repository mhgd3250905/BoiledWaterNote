package com.skkk.boiledwaternote.Presenters.NoteList;

import android.content.Context;

import com.skkk.boiledwaternote.Modles.DBUtils;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;

import java.util.List;

/*
* 
* 描    述：用于NoteList界面的管理
* 作    者：ksheng
* 时    间：2017/5/28$ 19:02$.
*/
public class NoteListPresenter implements NoteListable {
    private Context context;

    public NoteListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public List<Note> getNotes() {
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        List<Note> list = noteDao.queryBuilder().orderDesc(NoteDao.Properties.CreateTime).list();
        return list;
    }
}
