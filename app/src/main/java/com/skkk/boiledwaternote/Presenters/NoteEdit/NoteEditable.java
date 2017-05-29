package com.skkk.boiledwaternote.Presenters.NoteEdit;

import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.util.List;

/**
 * Created by admin on 2017/5/29.
 */

public interface NoteEditable {
    //通过分析RecyclerView中的列表转化为包含所有信息的字符串
    String analysisData2NoteStr(List<NoteEditModel> noteEditModels);
    //分析内容字符串保存Note
    void saveNote(String contentStr);
}
