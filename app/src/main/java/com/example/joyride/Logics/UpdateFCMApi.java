package com.example.joyride.Logics;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.joyride.ApiService.ApiService;
import com.example.joyride.Instance.RetrofitClient;
import com.example.joyride.Models.FcmRequest;
import com.example.joyride.Models.FcmResponse;
import com.example.joyride.Models.GPSResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFCMApi {
    private static final String TAG = "UpdateFCMApi";
    private final Context context;

    public UpdateFCMApi(Context context) {
        this.context = context.getApplicationContext();
    }

    public void updateFcm() {
        SessionManager sessionManager = new SessionManager(context);
        String jwtToken = sessionManager.getToken();

        FCMTokenManager tokenManager = new FCMTokenManager(context);
        String fcm = tokenManager.getToken();

        if (jwtToken == null || jwtToken.isEmpty() || fcm == null || fcm.isEmpty()) {
            Log.e(TAG, "JWT token or FCM token is null/empty");
            Toast.makeText(context, "Token error. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        FcmRequest request = new FcmRequest(fcm);

        apiService.updateFcm("Bearer " + jwtToken, request).enqueue(new Callback<FcmResponse>() {

            @Override
            public void onResponse(Call<FcmResponse> call, Response<FcmResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "FCM updated successfully: " + response.body());
                    Toast.makeText(context, "FCM token updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                        Log.e(TAG, "Failed to update FCM: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(context, "Failed to update FCM. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FcmResponse> call, Throwable t) {
                Log.e(TAG, "Error updating FCM: " + t.getMessage());
                Toast.makeText(context, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
