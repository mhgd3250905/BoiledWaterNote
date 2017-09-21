package com.skkk.boiledwaternote.Modles;

import android.content.Context;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.skkk.boiledwaternote.Modles.gen.NoteDao.Properties.NoteType;

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


    /**
     * 保存一个指定类型的笔记
     * @param noteType
     * @param note
     * @return
     */
    @Override
    public boolean saveOne(int noteType, Note note) {
        session = DBUtils.getInstance(context).getSession();
        try {
            NoteDao noteDao = session.getNoteDao();
            long insert = noteDao.insert(note);
            return (insert != -1) ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新笔记
     * @param note
     * @return
     */
    @Override
    public boolean updateOne(Note note) {
        session = DBUtils.getInstance(context).getSession();
        try {
            NoteDao noteDao = session.getNoteDao();
            noteDao.update(note);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除笔记
     * @param note
     * @return
     */
    @Override
    public boolean deleteOne(Note note) {
        session = DBUtils.getInstance(context).getSession();
        try {
            NoteDao noteDao = session.getNoteDao();
            noteDao.delete(note);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 查找指定类型的笔记
     * @param noteType
     * @return
     */
    @Override
    public List<Note> query(int noteType) {
        session = DBUtils.getInstance(context).getSession();
        QueryBuilder<Note> noteQueryBuilder = session.getNoteDao().queryBuilder();

        if (noteType == Note.NoteType.ALL_NOTE.getValue()) {
            noteQueryBuilder.where(noteQueryBuilder.or(NoteType.eq(Note.NoteType.ARTICLE_NOTE.getValue()),
                                    NoteType.eq(Note.NoteType.NOTE_NOTE.getValue())));
        } else {
            noteQueryBuilder.where(NoteType.eq(noteType));
        }

        List<Note> noteList = noteQueryBuilder.orderDesc(NoteDao.Properties.CreateTime).list();
        return noteList;
    }

    /**
     * 保存一个笔记
     * @param noteType
     * @param isNote
     * @param noteEditModels
     * @return
     */
    public boolean saveOne(int noteType, boolean isNote, NoteEditModel... noteEditModels) {
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
        note.setNoteType(noteType);
        return saveOne(noteType, note);
    }


}
