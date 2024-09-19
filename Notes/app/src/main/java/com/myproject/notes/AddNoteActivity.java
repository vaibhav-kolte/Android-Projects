package com.myproject.notes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myproject.notes.databinding.ActivityAddNoteBinding;
import com.myproject.notes.interfaces.SaveNoteInterface;
import com.myproject.notes.repository.FirebaseManager;

public class AddNoteActivity extends AppCompatActivity {

    private ActivityAddNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleOnClick();
    }

    private void handleOnClick() {
        binding.saveButton.setOnClickListener(view -> {
            String title = binding.etTitle.getText().toString();
            String noteContent = binding.etContent.getText().toString();
            FirebaseManager manager = new FirebaseManager();
            manager.saveNotes(title, noteContent, new SaveNoteInterface() {
                @Override
                public void onSuccess() {
                    finish();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(AddNoteActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}