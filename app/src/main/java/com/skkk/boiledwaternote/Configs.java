package com.skkk.boiledwaternote;

import android.text.Editable;

/**
 * Created by admin on 2017/5/29.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/29$ 18:55$.
*/
public class Configs {
    public static String DATE_TIME_FORMAT_LONG_CN = "yyyy年MM月dd日 HH:mm:ss";

    public static final int REQUEST_START_NEW_NOTE = 100;                     //新开启一个笔记
    public static final int REQUEST_UPDATE_NOTE = 101;                        //更新一个笔记
    public static final int REQUEST_DELETE_IMAGE = 102;                       //在编辑界面中打开图片详情然后删除图片
    public static final int REQUEST_PRIVACY_CHECK = 103;                      //点击隐私笔记进入保护界面

    public static final int RESULT_PRIVACY_CHECK_OK = 200;                   //点击隐私笔记进入保护界面验证成功
    public static final int RESULT_PRIVACY_CHECK_FAILED = 201;               //点击隐私笔记进入保护界面验证失败

    public static final String KEY_SAVE_NEW_NOTE = "result_save_new_note";    //保存一个新的笔记的获取数据的key
    public static final String KEY_UPDATE_NOTE = "key_update_note";           //更新笔记
    public static final String KEY_PREVIEW_IMAGE = "key_preview_image";       //预览的图片
    public static final String KEY_NOTE_TYPE = "key_note_type";  //预览的图片类型
    //笔记列表布局样式
    public static final String SP_KEY_NOTE_LIST_LAYOUT_STYLE = "sp_key_note_list_layout_style";
    public static final int NOTE_LIST_LAYOUT_STYLE_LINEAR = 0;
    public static final int NOTE_LIST_LAYOUT_STYLE_STAGGER = 1;

    //是否开启隐私模式
    public static final String SP_KEY_PRIVACY_ENABLE = "sp_key_privacy_enable";
    //隐私模式类型
    public static final String SP_KEY_PRIVACY_TYPE = "sp_key_privacy_type";
    public static final int PRIVACY_TYPE_TOUCH_ID = 0;
    public static final int PRIVACY_TYPE_GRAPHY = 1;


    public interface OnSelectionChangeListener {
        void onSelectionChangeListener(Editable s, int selStart, int selEnd);
    }

}
