package com.skkk.boiledwaternote.Modles;

import android.content.Context;

import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;

import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/6/11$ 15:43$.
*/
public class NoteModle implements ModleImpl<Note> {
    private Context context;
    private DaoSession session;

    public NoteModle(Context context) {
        this.context = context;
        session = DBUtils.getInstance(context).getSession();
    }

    @Override
    public List<Note> queryAll() {
        List<Note> noteList = session.getNoteDao().queryBuilder().orderDesc(NoteDao.Properties.CreateTime).list();
        return noteList;
    }

    @Override
    public boolean saveOne(Note note) {
        try {
            NoteDao noteDao = session.getNoteDao();
            long insert = noteDao.insert(note);
            return (insert==-1)?true:false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateOne(Note note) {
        try {
            NoteDao noteDao = session.getNoteDao();
            noteDao.update(note);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteOne(Note note) {
        try {
            NoteDao noteDao = session.getNoteDao();
            noteDao.delete(note);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Note queryLatestOne() {
        List<Note> notes = queryAll();
        return notes.get(0);
    }
}
