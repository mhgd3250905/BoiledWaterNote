package com.skkk.boiledwaternote.Modles;

import java.io.Serializable;

/**
 * Created by admin on 2017/4/23.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/23$ 19:21$.
*/
public class NoteEditModel implements Serializable {
    private String content;
    public Flag itemFlag;
    public String imagePath;
    public boolean format_align_center=false;  //居中
    public boolean format_bold=false;          //加粗
    public boolean format_italic=false;        //斜体
    public boolean format_list=false;          //列表
    public boolean format_list_numbered=false; //数字列表
    public boolean format_quote=false;         //引用
    public boolean format_title=false;         //标题字体
    public boolean format_underlined=false;    //下划线
    public boolean format_strike_through = false;//删除线
    public boolean format_show_checkbox=false;  //勾选框
    public boolean foramt_checkBox_check=false; //勾选框是否勾选

    public NoteEditModel() {
    }

    public NoteEditModel(String content, Flag itemFlag, String imagePath) {
        this.content = content;
        this.itemFlag = itemFlag;
        this.imagePath = imagePath;

    }

    public enum Flag {
        TEXT,
        IMAGE,
        SEPARATED,
        TIMERECORD
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setItemFlag(Flag itemFlag) {
        this.itemFlag = itemFlag;
    }

    public String getContent() {
        return content;
    }

    public Flag getItemFlag() {
        return itemFlag;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isFormat_align_center() {
        return format_align_center;
    }

    public void setFormat_align_center(boolean format_align_center) {
        this.format_align_center = format_align_center;
    }

    public boolean isFormat_italic() {
        return format_italic;
    }

    public void setFormat_italic(boolean format_italic) {
        this.format_italic = format_italic;
    }

    public boolean isFormat_list() {
        return format_list;
    }

    public void setFormat_list(boolean format_list) {
        this.format_list = format_list;
    }

    public boolean isFormat_list_numbered() {
        return format_list_numbered;
    }

    public void setFormat_list_numbered(boolean format_list_numbered) {
        this.format_list_numbered = format_list_numbered;
    }

    public boolean isFormat_quote() {
        return format_quote;
    }

    public void setFormat_quote(boolean format_quote) {
        this.format_quote = format_quote;
    }

    public boolean isFormat_title() {
        return format_title;
    }

    public void setFormat_title(boolean format_title) {
        this.format_title = format_title;
    }

    public boolean isFormat_underlined() {
        return format_underlined;
    }

    public void setFormat_underlined(boolean format_underlined) {
        this.format_underlined = format_underlined;
    }

    public boolean isFormat_bold() {
        return format_bold;
    }

    public void setFormat_bold(boolean format_bold) {
        this.format_bold = format_bold;
    }

    public boolean isFormat_strike_through() {
        return format_strike_through;
    }

    public void setFormat_strike_through(boolean format_strike_through) {
        this.format_strike_through = format_strike_through;
    }

    public boolean isFormat_show_checkbox() {
        return format_show_checkbox;
    }

    public boolean isForamt_checkBox_check() {
        return foramt_checkBox_check;
    }

    public void setFormat_show_checkbox(boolean format_show_checkbox, boolean foramt_checkBox_check) {
        this.format_show_checkbox = format_show_checkbox;
        setForamt_checkBox_check(foramt_checkBox_check);
    }

    public void setForamt_checkBox_check(boolean foramt_checkBox_check) {
        this.foramt_checkBox_check = foramt_checkBox_check;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteEditModel model = (NoteEditModel) o;

        if (format_align_center != model.format_align_center) return false;
        if (format_bold != model.format_bold) return false;
        if (format_italic != model.format_italic) return false;
        if (format_list != model.format_list) return false;
        if (format_list_numbered != model.format_list_numbered) return false;
        if (format_quote != model.format_quote) return false;
        if (format_title != model.format_title) return false;
        if (format_underlined != model.format_underlined) return false;
        if (format_strike_through != model.format_strike_through) return false;
        if (format_show_checkbox != model.format_show_checkbox) return false;
        if (foramt_checkBox_check != model.foramt_checkBox_check) return false;
        if (!content.equals(model.content)) return false;
        if (itemFlag != model.itemFlag) return false;
        return imagePath.equals(model.imagePath);

    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + itemFlag.hashCode();
        result = 31 * result + imagePath.hashCode();
        result = 31 * result + (format_align_center ? 1 : 0);
        result = 31 * result + (format_bold ? 1 : 0);
        result = 31 * result + (format_italic ? 1 : 0);
        result = 31 * result + (format_list ? 1 : 0);
        result = 31 * result + (format_list_numbered ? 1 : 0);
        result = 31 * result + (format_quote ? 1 : 0);
        result = 31 * result + (format_title ? 1 : 0);
        result = 31 * result + (format_underlined ? 1 : 0);
        result = 31 * result + (format_strike_through ? 1 : 0);
        result = 31 * result + (format_show_checkbox ? 1 : 0);
        result = 31 * result + (foramt_checkBox_check ? 1 : 0);
        return result;
    }
}
