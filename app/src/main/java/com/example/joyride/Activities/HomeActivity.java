package com.example.joyride.Activities;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.joyride.Fragments.ActivityFragment;
import com.example.joyride.Fragments.FutureFregment;
import com.example.joyride.Fragments.HistoryFragment;
import com.example.joyride.Fragments.HomeFragment;
import com.example.joyride.Fragments.ProfileFragment;
import com.example.joyride.Logics.FCMTokenManager;
import com.example.joyride.Logics.SessionManager;
import com.example.joyride.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fetchFCMToken();
        SessionManager sessionManager = new SessionManager(this);
        if(!sessionManager.isLoggedIn()){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Request notification permission for Android 13+
        requestNotificationPermission();
        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        // Set up BottomNavigationView item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_activity) {
                selectedFragment = new ActivityFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            else if (item.getItemId() == R.id.nav_future){
                selectedFragment = new FutureFregment();
            }
            else if(item.getItemId() == R.id.nav_history){
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
    // Request notification permission (required for Android 13+)
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ requires explicit permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
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

}
