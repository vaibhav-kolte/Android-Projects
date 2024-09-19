package com.myproject.notes;

import androidx.annotation.NonNull;

public class Note {
    private String id;
    private String title;
    private String note;

    // Empty constructor required for Firebase
    public Note() {
    }

    public Note(String id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    @NonNull
    @Override
    public String toString() {
        return "id: " + id +
                "\nTitle: " + title +
                "\nContent: " + note;
    }
}

