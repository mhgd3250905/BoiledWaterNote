package com.skkk.boiledwaternote.Modles;

/**
 * Created by admin on 2017/4/23.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/4/23$ 19:21$.
*/
public class NoteEditModel {
    private String content;
    public Flag itemFlag;
    public String imagePath;
    public boolean format_align_center=false;  //居中
    public boolean format_bold=false;          //加粗
    public boolean format_italic=false;        //斜体
    public boolean format_list=false;          //列表
    public boolean format_list_numbered=false; //数字列表
    public boolean format_quote=false;         //引用
    public int format_size=1;                  //字体大小：0-p 1-h1 2-h2 3-h3
    public boolean format_underlined=false;    //下划线
    public boolean format_strike_through = false;//删除线

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
        SEPARATED
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

    public int getFormat_size() {
        return format_size;
    }

    public void setFormat_size(int format_size) {
        this.format_size = format_size;
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
}
