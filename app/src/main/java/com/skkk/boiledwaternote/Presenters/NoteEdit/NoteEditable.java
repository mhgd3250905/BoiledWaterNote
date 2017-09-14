package com.skkk.boiledwaternote.Presenters.NoteEdit;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.util.List;

/**
 * Created by admin on 2017/5/29.
 */

public interface NoteEditable {
    //分析内容字符串保存Note
    boolean saveNote(boolean isNote,List<NoteEditModel> noteEditModels);

    //保存Note
    boolean saveNote(boolean isNote,NoteEditModel... noteEditModels);

    //分析内容更新笔记
    boolean updateNote(List<NoteEditModel> noteEditModels, Note note);
}
