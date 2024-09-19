package com.myproject.notes.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseClient {

    private static DatabaseReference databaseReference;

    public static DatabaseReference getClient() {

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        }
        return databaseReference;
    }
}
