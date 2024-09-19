package com.myproject.notes.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myproject.notes.Note;
import com.myproject.notes.interfaces.GetNotesInterface;
import com.myproject.notes.interfaces.SaveNoteInterface;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager implements NoteRepository {

    @Override
    public void saveNotes(String title, String content, SaveNoteInterface saveNoteInterface) {
        String id = FirebaseClient.getClient().push().getKey();
        Note note = new Note(id, title, content);

        if (id != null) {
            FirebaseClient.getClient().child(id).setValue(note)
                    .addOnSuccessListener(aVoid -> saveNoteInterface.onSuccess())
                    .addOnFailureListener(e -> saveNoteInterface.onFailure("Filed to save note. Exception: " + e.getMessage()));
        }
    }

    @Override
    public void getNotes(GetNotesInterface notesInterface) {
        List<Note> notes = new ArrayList<>();
        FirebaseClient.getClient().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    assert note != null;
                    notes.add(note);
                    Log.d("MainActivity", "Note: " + note.getTitle() + " Content: " + note.getNote());
                }
                notesInterface.getNotes(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "Failed to read notes.", error.toException());
            }
        });
    }

    @Override
    public void updateNotes(Note note) {

    }

    @Override
    public void deleteNotes(Note note) {

    }
}
