package com.myproject.notes.repository;

import com.myproject.notes.Note;
import com.myproject.notes.interfaces.GetNotesInterface;
import com.myproject.notes.interfaces.SaveNoteInterface;

public interface NoteRepository {
    void saveNotes(String title, String content, SaveNoteInterface saveNoteInterface);
    void getNotes(GetNotesInterface notesInterface);
    void updateNotes(Note note);
    void deleteNotes(Note note);
}
