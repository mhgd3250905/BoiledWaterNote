package com.skkk.boiledwaternote.Presenters.NoteEdit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.DBUtils;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteImage;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Modles.gen.NoteImageDao;

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
    private final String TAG=NoteEditPresenter.class.getSimpleName();

    private String SEPARATED_FLAG="☞";      //用来分隔不同条目传入的不同数据
    private String SEPARATED_TEXT_FLAG="$|TEXT|$";      //用来分隔不同条目传入的不同数据
    private String SEPARATED_IMAGE_FLAG="$|IMAGE|$";      //用来分隔不同条目传入的不同数据
    private Context context;

    public NoteEditPresenter(Context context) {
        this.context = context;
    }


    /**
     * 保存Note
     * @param noteEditModels    内容字符串
     */
    @Override
    public void saveNote(List<NoteEditModel> noteEditModels) {

        Gson gson=new Gson();
        String contentJson = gson.toJson(noteEditModels);
        Log.i(TAG, "saveNote: json---------------------->"+"\n"+contentJson);

        //获取数据库操作类
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        NoteImageDao noteImageDao = session.getNoteImageDao();

        Note note=new Note();
        note.setNid(System.currentTimeMillis());
        note.setContent(contentJson);
        note.setCreateTime(new Date());
        note.setUpdateTime(new Date());
        noteDao.insert(note);
        Log.i(TAG, "saveNote: "+note.toString());

        for (int i = 0; i < noteEditModels.size(); i++) {
            if (noteEditModels.get(i).getItemFlag()==NoteEditModel.Flag.IMAGE){
                NoteImage noteImage=new NoteImage();
                noteImage.setNoteId(note.getNid());
                noteImage.setPath(noteEditModels.get(i).getImagePath());
                noteImageDao.insert(noteImage);
                Log.i(TAG, "saveImage: "+noteImage.toString());
            }
        }
    }
}
