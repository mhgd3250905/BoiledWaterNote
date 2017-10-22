package com.skkk.boiledwaternote.Modles;

import android.content.Context;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Modles.gen.DaoSession;
import com.skkk.boiledwaternote.Modles.gen.NoteDao;
import com.skkk.boiledwaternote.Views.NoteImage.ImageModle;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
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
public class NoteModle implements NoteModleImpl<Note> {
    private Context context;
    private DaoSession session;

    public NoteModle(Context context) {
        this.context = context;
    }


    /**
     * 保存一个指定类型的笔记
     *
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
     *
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
     * 删除笔记: 删除笔记如果是回收站类型的时候就直接删除，否则设置类型为回收站类型
     *
     * @param note
     * @return
     */
    @Override
    public boolean deleteOne(Note note) {
        session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        try {
            if (note.getNoteType() == Note.NoteType.RECYCLE_NOTE.getValue()) {
                noteDao.delete(note);
                return true;
            } else {
                note.setNoteType(Note.NoteType.RECYCLE_NOTE.getValue());
                noteDao.update(note);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量删除笔记: 删除笔记如果是回收站类型的时候就直接删除，否则设置类型为回收站类型
     *
     * @param notes
     * @return
     */
    @Override
    public boolean deleteAll(List<Note> notes) {
        session = DBUtils.getInstance(context).getSession();
        NoteDao noteDao = session.getNoteDao();
        boolean done=true;
        try {
            for (Note note : notes) {
                if (note.getNoteType() == Note.NoteType.RECYCLE_NOTE.getValue()) {
                    noteDao.delete(note);
                } else {
                    note.setNoteType(Note.NoteType.RECYCLE_NOTE.getValue());
                    noteDao.update(note);
                }
            }

        } catch (Exception e) {
            done=false;
        }
        return done;
    }

    /**
     * 查找指定类型的笔记
     *
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
     * 查找所有的图片
     *
     * @return
     */
    @Override
    public List<NoteEditModel> queryAllImages(int type) {
        List<NoteEditModel> modelList = new ArrayList<>();
        List<Note> allNotes = query(type);
        String content;
        for (Note note : allNotes) {
            content = "";
            content = note.getContent();
            NoteEditModel[] models = new Gson().fromJson(content, NoteEditModel[].class);
            for (NoteEditModel model : models) {
                if (model.getItemFlag() == NoteEditModel.Flag.IMAGE) {
                    modelList.add(model);
                }
            }
        }
        return modelList;
    }

    /**
     * 删除一个图片
     *
     * @return
     */
    @Override
    public boolean deleteImage(ImageModle imageModle, int type) {
        boolean done = false;
        List<Note> allNotes = query(type);
        String content;//笔记内容
        for (Note note : allNotes) {
            content = "";
            content = note.getContent();
            NoteEditModel[] models = new Gson().fromJson(content, NoteEditModel[].class);
            List<NoteEditModel> modelList = Arrays.asList(models);
            /*
            * 如果条目中有这个图片条目就直接删除掉
            * */
            List<NoteEditModel> newModleList = new ArrayList<>();
            newModleList.addAll(modelList);
            for (int i = 0; i < modelList.size(); i++) {
                NoteEditModel model = modelList.get(i);
                NoteEditModel model1 = imageModle.getnoteEditModel();
                if (modelList.get(i).equals(imageModle.getnoteEditModel())) {
                    newModleList.remove(i);
                    note.setContent(new Gson().toJson(newModleList));
                    done = updateOne(note);
                    break;
                }
            }
        }
        return done;
    }

    /**
     * 删除批量图片
     *
     * @return
     */
    @Override
    public boolean deleteAllImages(List<ImageModle> modelList, int type) {
        boolean done = true;
        for (int i = 0; i < modelList.size(); i++) {
            if (!deleteImage(modelList.get(i), type)) {
                done = false;
            }
        }
        return done;
    }

    /**
     * 保存一个笔记
     *
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
