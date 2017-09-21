package com.skkk.boiledwaternote.Presenters.NoteList;

import com.skkk.boiledwaternote.Modles.Note;

/**
 * Created by admin on 2017/5/28.
 */

public interface NoteListable {
    void showNotes(int noteType);
    void showAllNote();
    void deleteNote(int pos);
    void updateNoteToPrivacy(int pos, int showNoteType);
    Note getNote(int pos);
    void saveNote(String noteContent);
    void startEditActivity(int pos);
    void showSpecialTypeNotes(int noteType);
    void showAllImages();
}
