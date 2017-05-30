package com.skkk.boiledwaternote.Presenters.NoteEdit;

import android.content.Context;
import android.util.Log;

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
     * 将编辑界面中的数据分析并转化为Note
     * @return 一串包含了所有note信息的字符串
     */
    @Override
    public String analysisData2NoteStr(List<NoteEditModel> noteEditModels){
        StringBuffer sbContent=new StringBuffer();

        for (int i = 0; i < noteEditModels.size(); i++) {
            NoteEditModel model=noteEditModels.get(i);          //获取一个条目
            switch (model.getItemFlag()){
                case TEXT:                                      //文本类型保存文本
                    sbContent.append(SEPARATED_TEXT_FLAG);
                    sbContent.append(model.getContent());
                    sbContent.append(SEPARATED_FLAG);
                    break;
                case IMAGE:                                     //图片类型保存图片链接
                    sbContent.append(SEPARATED_IMAGE_FLAG);
                    sbContent.append(model.getImagePath());
                    sbContent.append(SEPARATED_FLAG);
                    break;
            }
        }
        Log.i(TAG, "analysisData2NoteStr: "+sbContent.toString());
        return sbContent.toString();
    }

    /**
     * 保存Note
     * @param contentStr    内容字符串
     */
    @Override
    public void saveNote(String contentStr) {
        //获取数据库操作类
        DaoSession session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        NoteImageDao noteImageDao = session.getNoteImageDao();

        Note note=new Note();
        note.setNid(System.currentTimeMillis());
        note.setContent(contentStr);
        note.setCreateTime(new Date());
        note.setUpdateTime(new Date());
        noteDao.insert(note);
        Log.i(TAG, "saveNote: "+note.toString());

        String[] contentArr = contentStr.split(SEPARATED_FLAG);
        for (int i = 0; i < contentArr.length; i++) {
            if (contentArr[i]==null){
                break;
            }
            if (contentArr[i].startsWith(SEPARATED_IMAGE_FLAG)){
                String imagePath=contentArr[i].substring(SEPARATED_FLAG.length(),contentArr[i].length());
                NoteImage noteImage=new NoteImage();
                noteImage.setNoteId(note.getNid());
                noteImage.setPath(imagePath);
                noteImageDao.insert(noteImage);
                Log.i(TAG, "saveImage: "+noteImage.toString());
            }
        }
    }
}
