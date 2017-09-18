package com.skkk.boiledwaternote.Presenters.NoteEdit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.DBUtils;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Modles.gen.NoteImageDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/5/28.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 23:38$.
*/
public class NoteEditPresenter implements NoteEditable {
    private final String TAG = NoteEditPresenter.class.getSimpleName();
    private Context context;
    private NoteModle noteModlel;

    public NoteEditPresenter(Context context) {
        this.context = context;
        noteModlel = new NoteModle(context);
    }

    /**
     * 保存Note
     *
     * @param noteEditModels 内容字符串
     */
    @Override
    public boolean saveNote(int noteType, boolean isNote, List<NoteEditModel> noteEditModels) {
        Gson gson = new Gson();
        String contentJson = gson.toJson(noteEditModels);
        Log.i(TAG, "saveNote: json---------------------->" + "\n" + contentJson);

        //获取数据库操作类
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        NoteImageDao noteImageDao = session.getNoteImageDao();

        Note note = new Note();
        note.setNid(System.currentTimeMillis());
        note.setContent(contentJson);
        note.setCreateTime(new Date());
        note.setUpdateTime(new Date());
        note.setIsNote(isNote);
        note.setNoteType(noteType);

        boolean save = noteModlel.saveOne(note);
        Log.i(TAG, "getNote: " + note.toString());
        return save;

    }


    /**
     * 保存Note
     *
     * @param noteEditModels
     * @return
     */
    @Override
    public boolean saveNote(int noteType, boolean isNote, NoteEditModel... noteEditModels) {
        List<NoteEditModel> noteEditViewHolderList = new ArrayList<>();

        for (int i = 0; i < noteEditModels.length; i++) {
            noteEditViewHolderList.add(noteEditModels[i]);
        }

        Gson gson = new Gson();
        String contentJson = gson.toJson(noteEditViewHolderList);
        Log.i(TAG, "saveNote: json---------------------->" + "\n" + contentJson);

        //获取数据库操作类
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        NoteImageDao noteImageDao = session.getNoteImageDao();

        Note note = new Note();
        note.setNid(System.currentTimeMillis());
        note.setContent(contentJson);
        note.setCreateTime(new Date());
        note.setUpdateTime(new Date());
        note.setIsNote(isNote);
        note.setNoteType(noteType);

        boolean save = noteModlel.saveOne(note);
        Log.i(TAG, "getNote: " + note.toString());
        return save;

    }

    /**
     * 更新笔记
     *
     * @param noteEditModels
     * @return
     */
    @Override
    public boolean updateNote(List<NoteEditModel> noteEditModels, Note note) {

        Gson gson = new Gson();
        String contentJson = gson.toJson(noteEditModels);
        Log.i(TAG, "updateNote: json---------------------->" + "\n" + contentJson);

        //获取数据库操作类
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();


        note.setContent(contentJson);
        note.setUpdateTime(new Date());

        boolean update = noteModlel.updateOne(note);
        Log.i(TAG, "getNote: " + note.toString());
        return update;
    }
}
