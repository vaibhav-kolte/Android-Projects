package com.myproject.notes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myproject.notes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setOnClickHandler();
    }

    private void setOnClickHandler() {
        binding.fabButton.setOnClickListener(view -> {
            Toast.makeText(this, "Replace your action!", Toast.LENGTH_SHORT).show();
        });
    }
}