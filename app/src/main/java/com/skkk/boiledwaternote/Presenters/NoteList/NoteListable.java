package com.skkk.boiledwaternote.Presenters.NoteList;

import com.skkk.boiledwaternote.Modles.Note;

import java.util.List;

/**
 * Created by admin on 2017/5/28.
 */

public interface NoteListable {
    void showNotes(String noteType);
    List<Note> getNotes();
    void showAllNote();
    boolean deleteNote(Note note);
    boolean updateNote(Note note);
}
