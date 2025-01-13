package com.example.joyride.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joyride.Logics.LoginManager;
import com.example.joyride.Models.UserProfileResponse;
import com.example.joyride.R;

public class ProfileFragment extends Fragment {

    private TextView driverName;
    private TextView phoneNumber;
    private TextView email;
    private ImageButton navIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        driverName = view.findViewById(R.id.textViewDriverName);
        phoneNumber = view.findViewById(R.id.textViewDriverPhone);
        email = view.findViewById(R.id.textViewDriverEmail);
        navIcon = view.findViewById(R.id.nav_icon);

        // Handle navigation icon click for the popup menu
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(getContext(), navIcon);
                popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

                // Handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_statement) {
                            Toast.makeText(getContext(), "Settings selected", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (item.getItemId() == R.id.menu_documents) {
                            Toast.makeText(getContext(), "Share selected", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (item.getItemId() == R.id.menu_earnings) {
                            Toast.makeText(getContext(), "About Us selected", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            return false;
                        }
                    }


                });

                // Show the popup menu
                popupMenu.show();
            }
        });

        // Fetch profile details
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
