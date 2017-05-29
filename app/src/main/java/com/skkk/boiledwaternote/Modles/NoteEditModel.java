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

    public NoteEditModel(String content, Flag itemFlag, String imagePath) {
        this.content = content;
        this.itemFlag = itemFlag;
        this.imagePath = imagePath;
    }

    public enum Flag {
        TEXT,
        IMAGE
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
}
