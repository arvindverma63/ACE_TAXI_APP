package com.example.joyride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.joyride.Logics.FCMTokenManager;
import com.example.joyride.sliders.Slider2;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Hide system UI for immersive experience
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        // Request notification permission for Android 13+
        requestNotificationPermission();
        // Fetch FCM token
        fetchFCMToken();
    }

    // Request notification permission (required for Android 13+)
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ requires explicit permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Fetch and log the FCM token
    private void fetchFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        FCMTokenManager fcmTokenManager = new FCMTokenManager(this);
                        fcmTokenManager.setToken(token);
                        // Optionally send the token to a server for device registration
                        sendTokenToServer(token);
                    } else {
                        Log.e(TAG, "Failed to fetch FCM token", task.getException());
                    }
                });
    }

    // Stub for sending the token to the server (implement as needed)
    private void sendTokenToServer(String token) {
        // Example: Send token to your backend server
        Log.d(TAG, "Token sent to server: " + token);
    }

    // Handle notification permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted.");
            } else {
                Log.e(TAG, "Notification permission denied.");
            }
        }
    }

    // Handle "Continue" button click
    public void continueButton(View view) {
        Intent intent = new Intent(this, Slider2.class);
        startActivity(intent);
    }

    // Handle "Skip" button click (currently empty)
    public void skipButton(View view) {
        Log.d(TAG, "Skip button clicked");
        FCMTokenManager fcmTokenManager = new FCMTokenManager(this);
        String token = fcmTokenManager.getToken();
        Log.d(TAG,""+token);
    }
}
