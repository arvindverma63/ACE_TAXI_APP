package com.example.joyride.ApiService;

import com.example.joyride.Models.FcmRequest;
import com.example.joyride.Models.FcmResponse;
import com.example.joyride.Models.GPSRequest;
import com.example.joyride.Models.GPSResponse;
import com.example.joyride.Models.LoginRequest;
import com.example.joyride.Models.LoginResponse;
import com.example.joyride.Models.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/Auth/Authenticate")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/DriverApp/UpdateGPS")
    Call<GPSResponse> updateGps(
            @Header("Authorization") String token, // Pass the Bearer token for authentication
            @Body GPSRequest gpsRequest
    );

    @POST("/api/DriverApp/UpdateFCM")
    Call<FcmResponse> updateFcm(
            @Header("Authorization") String token,
            @Body FcmRequest fcmRequest
    );

    @GET("/api/UserProfile/GetUser")
    Call<UserProfileResponse> userProfile(@Query("username")String username,
                                          @Header("Authorization") String token);

}
