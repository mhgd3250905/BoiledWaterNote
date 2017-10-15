package com.skkk.boiledwaternote.Views.PrivacyProtect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

/**
 * 创建于 2017/10/15
 * 作者 admin
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/10/15$ 23:15$.
*/
public abstract class BasePrivacyActivity extends AppCompatActivity {
    protected Note updateNote;
    protected int noteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    /**
     * 跳转到编辑页面
     */
    protected void startNoteActivity(){
        Intent intent = new Intent();
        intent.setClass(BasePrivacyActivity.this, NoteEditActivity.class);
        intent.putExtra(Configs.KEY_UPDATE_NOTE, updateNote);
        intent.putExtra(Configs.KEY_NOTE_TYPE, noteType);
        startActivityForResult(intent, Configs.REQUEST_UPDATE_NOTE);
    }

    /**
     * 获取传入数据
     */
    private void getIntentData(){
        Intent intent = getIntent();
        updateNote = (Note) intent.getSerializableExtra(Configs.KEY_UPDATE_NOTE);
        noteType = intent.getIntExtra(Configs.KEY_NOTE_TYPE, 0);
    }
}
