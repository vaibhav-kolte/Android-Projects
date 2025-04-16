package com.vk.csvcreater;

import static com.vk.csvcreater.CsvHelper.saveCSV;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vk.csvcreater.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleButtonClick();
    }

    private void handleButtonClick() {
        binding.btnSave.setOnClickListener(v -> {
            saveCSV(binding.getRoot().getContext(),
                    "files",
                    "" + binding.etFirstName.getText(),
                    "" + binding.etLastName.getText(),
                    "" + binding.etMobileNumber.getText());
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etFirstName.setText("");
            binding.etLastName.setText("");
            binding.etMobileNumber.setText("");
        });
    }
}