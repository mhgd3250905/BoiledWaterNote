package com.skkk.boiledwaternote.Presenters.NoteEdit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.DBUtils;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
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
    public NoteEditPresenter(Context context) {
        this.context = context;
    }

    /**
     * 保存Note
     *
     * @param noteEditModels 内容字符串
     */
    @Override
    public boolean saveNote(List<NoteEditModel> noteEditModels) {
        boolean done = false;

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

        done = noteDao.insert(note) != -1;
        Log.i(TAG, "getNote: " + note.toString());
        return done;

    }


    /**
     * 保存Note
     * @param noteEditModels
     * @return
     */
    @Override
    public boolean saveNote(NoteEditModel... noteEditModels) {
        boolean done = false;
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

        done = noteDao.insert(note) != -1;
        Log.i(TAG, "getNote: " + note.toString());
        return done;

    }

    /**
     * 更新笔记
     * @param noteEditModels
     * @return
     */
    @Override
    public boolean updateNote(List<NoteEditModel> noteEditModels,Note note) {

        boolean done = false;
        try {
            Gson gson = new Gson();
            String contentJson = gson.toJson(noteEditModels);
            Log.i(TAG, "updateNote: json---------------------->" + "\n" + contentJson);

            //获取数据库操作类
            DaoSession session = DBUtils.getInstance(context).getSession();
            NoteDao noteDao = session.getNoteDao();


            note.setContent(contentJson);
            note.setUpdateTime(new Date());

            noteDao.update(note);
            Log.i(TAG, "getNote: " + note.toString());
            done=true;
        }catch (Exception e){
            done=false;
        }
        return done;
    }
}
