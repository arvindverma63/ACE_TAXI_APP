package com.example.joyride.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joyride.Logics.LoginManager;
import com.example.joyride.Models.UserProfileResponse;
import com.example.joyride.R;


public class ProfileFragment extends Fragment {

    private TextView driverName;
    private TextView phoneNumber;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        driverName = view.findViewById(R.id.textViewDriverName);
        phoneNumber = view.findViewById(R.id.textViewDriverPhone);
        email = view.findViewById(R.id.textViewDriverEmail);

        LoginManager loginManager = new LoginManager(getContext());
        loginManager.getProfile(new LoginManager.ProfileCallback() {
            @Override
            public void onSuccess(UserProfileResponse userProfileResponse) {
                driverName.setText(userProfileResponse.getFullName());
                phoneNumber.setText(userProfileResponse.getPhoneNumber());
                email.setText(userProfileResponse.getEmail());
            }

            @Override
            public void onFailure(String errorMessage) {
                // Show error message
                Toast.makeText(getContext(), "Failed to fetch profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }



}