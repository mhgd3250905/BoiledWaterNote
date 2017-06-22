package com.skkk.boiledwaternote.CostomViews.RichEdit;

import com.skkk.boiledwaternote.Modles.NoteEditModel;

import java.util.List;

/**
 * 描述富文本编辑框的接口
 * Created by admin on 2017/6/22.
 */

public interface RichEditable {
    //开始崭新的编辑文本
    void resetRichText();
    //加载传入的富文本
    void loadRichText(List<NoteEditModel> richTexts);
    //获取数据
    List<NoteEditModel> getRichText();
}
