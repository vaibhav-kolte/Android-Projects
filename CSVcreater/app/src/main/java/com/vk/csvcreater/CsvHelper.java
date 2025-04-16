package com.vk.csvcreater;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvHelper {

    private static final String TAG = "CsvHelper";

    public static void saveCSV(@NonNull Context context, String fileName, String firstName,
                               String lastName, String mobileNumber) {
        String csvHeader = "First Name,Last Name,Mobile Number";
        String csvData = firstName + "," + lastName + "," + mobileNumber;

        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName + ".csv");
        boolean isNewFile = !file.exists();

        try {
            FileWriter writer = new FileWriter(file, true); // true = append mode

            if (isNewFile) {
                writer.append(csvHeader).append("\n"); // write header only once
            }

            writer.append(csvData).append("\n"); // append data row
            writer.flush();
            writer.close();

            Log.d(TAG, "CSV saved at: " + file.getAbsolutePath());
            Toast.makeText(context, "Record saved.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "saveCSV: Error: " + e.getMessage());
            Toast.makeText(context, "Failed to save CSV", Toast.LENGTH_SHORT).show();
        }
    }
}
