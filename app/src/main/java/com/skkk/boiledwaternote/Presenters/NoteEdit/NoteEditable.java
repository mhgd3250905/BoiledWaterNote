package com.skkk.boiledwaternote.Presenters.NoteEdit;

import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.util.List;

/**
 * Created by admin on 2017/5/29.
 */

public interface NoteEditable {
    //分析内容字符串保存Note
    void saveNote(List<NoteEditModel> noteEditModels);
}
