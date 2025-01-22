package com.vkolte.backgroundservices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MyBackgroundService extends Service {
    private static final String TAG = "MyLocationService";
    private static final String CHANNEL_ID = "LocationServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        setupForegroundService();
        initLocationServices();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        startLocationUpdates();
        return START_STICKY; // Restart service if killed by the system
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Non-bound service
    }

    /**
     * Sets up the foreground service with a persistent notification.
     */
    @SuppressLint("ForegroundServiceType")
    private void setupForegroundService() {
        createNotificationChannel();
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service Running")
                .setContentText("Fetching location every 5 seconds.")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Creates the notification channel for foreground service notifications.
     */
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Initializes location services and defines the location callback.
     */
    private void initLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                Log.d(TAG, "Location: " + location.getLatitude() + ", " + location.getLongitude());
            }
        };
    }

    /**
     * Starts location updates every 5 seconds.
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // Update interval in milliseconds
        locationRequest.setFastestInterval(3000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                    handler == null ? null : handler.getLooper());
        } else {
            Log.e(TAG, "Location permission not granted.");
        }
    }

    /**
     * Stops location updates.
     */
    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
