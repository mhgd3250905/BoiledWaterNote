package com.skkk.boiledwaternote.Modles;

import android.content.Context;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Views.Home.NoteListFragment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
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
    }

    @Override
    public List<Note> queryAll() {
        session = DBUtils.getInstance(context).getSession();
        List<Note> noteList = session.getNoteDao().queryBuilder().orderDesc(NoteDao.Properties.CreateTime).list();
        return noteList;
    }

    @Override
    public List<Note> query(String noteType) {
        session = DBUtils.getInstance(context).getSession();
        QueryBuilder<Note> noteQueryBuilder = session.getNoteDao().queryBuilder();
        switch (noteType) {
            case NoteListFragment.NOTE_TYPE_NONE:
                noteQueryBuilder.where(
                        noteQueryBuilder.and(NoteDao.Properties.IsPrivacy.eq(false),
                        noteQueryBuilder.or(NoteDao.Properties.NoteType.eq(1),
                                NoteDao.Properties.NoteType.eq(2))));
                break;
            case NoteListFragment.NOTE_TYPE_ARTICLE:
                noteQueryBuilder.where(
                        noteQueryBuilder.and(NoteDao.Properties.IsPrivacy.eq(false),
                                NoteDao.Properties.NoteType.eq(1)));
                break;
            case NoteListFragment.NOTE_TYPE_NOTE:
                noteQueryBuilder.where(noteQueryBuilder.and(NoteDao.Properties.NoteType.eq(2),
                        NoteDao.Properties.IsPrivacy.eq(false)));
                break;
            case NoteListFragment.NOTE_TYPE_PRIVACY:
                noteQueryBuilder.where(NoteDao.Properties.IsPrivacy.eq(true));
                break;

        }
        List<Note> noteList = noteQueryBuilder.orderDesc(NoteDao.Properties.CreateTime).list();
        return noteList;
    }

    @Override
    public boolean saveOne(Note note) {
        session = DBUtils.getInstance(context).getSession();
        try {
            NoteDao noteDao = session.getNoteDao();
            long insert = noteDao.insert(note);
            return (insert!=-1)?true:false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateOne(Note note) {
        session = DBUtils.getInstance(context).getSession();
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
        session = DBUtils.getInstance(context).getSession();
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

    public boolean saveNote(int noteType,boolean isNote,NoteEditModel... noteEditModels){
        List<NoteEditModel> noteEditViewHolderList = new ArrayList<>();

        for (int i = 0; i < noteEditModels.length; i++) {
            noteEditViewHolderList.add(noteEditModels[i]);
        }

        Gson gson = new Gson();
        String contentJson = gson.toJson(noteEditViewHolderList);

        Note note = new Note();
        note.setNid(System.currentTimeMillis());
        note.setContent(contentJson);
        note.setCreateTime(new Date());
        note.setUpdateTime(new Date());
        note.setIsNote(isNote);
        note.setNoteType(noteType);
        return saveOne(note);
    }


}
