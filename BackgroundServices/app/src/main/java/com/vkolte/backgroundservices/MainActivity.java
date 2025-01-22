package com.vkolte.backgroundservices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private final String[] requiredPermissions = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private ActivityResultLauncher<Intent> batteryOptimizationLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register launcher for battery optimization
        batteryOptimizationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> Toast.makeText(
                        this,
                        "Ensure battery optimization is disabled for proper functionality.",
                        Toast.LENGTH_LONG
                ).show()
        );

        // Check location permissions
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        boolean hasAllPermissions = true;

        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasAllPermissions = false;
                break;
            }
        }

        if (hasAllPermissions) {
            // Permissions granted, proceed
            startBackgroundService();
            requestIgnoreBatteryOptimizations();
        } else {
            // Request missing permissions
            ActivityCompat.requestPermissions(this, requiredPermissions, 100);
        }
    }

    private void startBackgroundService() {
        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        startService(serviceIntent);
    }

    private void requestIgnoreBatteryOptimizations() {
        @SuppressLint("BatteryLife")
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        batteryOptimizationLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // Permissions granted
                startBackgroundService();
                requestIgnoreBatteryOptimizations();
            } else {
                // Permissions denied
                Toast.makeText(
                        this,
                        "Location permissions are required for this app to function properly.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}