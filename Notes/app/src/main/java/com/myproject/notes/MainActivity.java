package com.myproject.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.myproject.notes.adapters.NotesAdapter;
import com.myproject.notes.databinding.ActivityMainBinding;
import com.myproject.notes.repository.FirebaseManager;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        context = MainActivity.this;

        getNotesFromFirebase();
        setOnClickHandler();
    }


    private void getNotesFromFirebase() {
        FirebaseManager manager = new FirebaseManager();
        manager.getNotes(noteList -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (Note note : noteList) {
                stringBuilder.append(note);
            }
            NotesAdapter adapter = new NotesAdapter(noteList);
            binding.noteRv.setHasFixedSize(true);
            binding.noteRv.setLayoutManager(new GridLayoutManager(context, 2));
            binding.noteRv.setAdapter(adapter);
        });
    }


    private void setOnClickHandler() {
        binding.fabButton.setOnClickListener(view -> {
            startActivity(new Intent(context, AddNoteActivity.class));
        });
    }
}