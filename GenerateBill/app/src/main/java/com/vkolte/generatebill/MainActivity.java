package com.vkolte.generatebill;

import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vkolte.generatebill.databinding.ActivityMainBinding;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleOnClick();
    }

    private void handleOnClick() {
        binding.btnGenerateBill.setOnClickListener(v -> {
//            Toast.makeText(this, "this", Toast.LENGTH_SHORT).show();
            try {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet hssfSheet = hssfWorkbook.createSheet("MySheet");

                HSSFRow hssfRow = hssfSheet.createRow(0);

                HSSFCell hssfCell = hssfRow.createCell(0);
                hssfCell.setCellValue("Vaibhav");

                saveWorkBook(hssfWorkbook);
            } catch (Exception e) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "handleOnClick: " + e.getMessage());
            }
        });
    }

    private void saveWorkBook(HSSFWorkbook hssfWorkbook) {
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            File fileOutput = new File(Objects.requireNonNull(storageVolume.getDirectory()).getPath() + "/Download/Program.xls");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
                hssfWorkbook.write(fileOutputStream);
                fileOutputStream.close();
                hssfWorkbook.close();
                Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "File Failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "saveWorkBook: " + e.getMessage());
            }
        }
    }
}