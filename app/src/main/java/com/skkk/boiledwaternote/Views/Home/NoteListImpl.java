package com.skkk.boiledwaternote.Views.Home;

import com.skkk.boiledwaternote.Modles.Note;

import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */

public interface NoteListImpl {
    void showList(List<Note> noteList);
    void deleteNote(int pos);
    void deletelist(List<Note> noteList);
}
