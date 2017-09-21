package com.skkk.boiledwaternote.Views.Home;

import android.support.annotation.StringRes;

import com.skkk.boiledwaternote.Modles.Note;

import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */

public interface NoteListImpl {
    void showList(List<Note> noteList);
    void deleteNote(int pos);
    void deletelist(List<Note> noteList);
    void insertNote(int pos);
    void clearNoteEditText();
    void resetAdapterData(List<Note> dataList);
    void showNotice(@StringRes int strId);
    void startActivity(Note note);
    void changNoteType();
}
