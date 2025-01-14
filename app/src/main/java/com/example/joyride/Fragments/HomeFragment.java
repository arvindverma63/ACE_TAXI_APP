package com.example.joyride.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joyride.Activities.LoginActivity;
import com.example.joyride.Logics.MapUpdate;
import com.example.joyride.Logics.Services.LocationService;
import com.example.joyride.Logics.SessionManager;
import com.example.joyride.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "HomeFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int BATTERY_OPTIMIZATION_REQUEST_CODE = 1002;

    private Switch location_switch;
    private TextView location_status;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SessionManager sessionManager = new SessionManager(getContext());
        if(!sessionManager.isLoggedIn()){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        // Initialize the Switch
        location_switch = rootView.findViewById(R.id.location_switch);
        location_status = rootView.findViewById(R.id.location_status);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        // Add a listener to the Switch
        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Switch toggled: " + isChecked);

                if (isChecked) {
                    // Check if GPS is enabled
                    if (isLocationEnabled()) {
                        // Check location permissions and battery optimization before starting the service
                        if (checkLocationPermissions() && checkBatteryOptimizations()) {
                            startLocationService();
                        } else {
                            requestLocationPermissions();
                            location_switch.setChecked(false); // Reset switch to OFF if permissions are not granted
                        }
                    } else {
                        Log.e(TAG, "Location services (GPS) are disabled.");
                        promptEnableGPS();
                        location_switch.setChecked(false); // Reset the switch
                    }
                } else {
                    stopLocationService();
                }
            }
        });


        return rootView; // Ensure this is the last line
    }

    private boolean checkLocationPermissions() {
        boolean hasFineLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarseLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean hasBackgroundLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (!hasBackgroundLocation) {
                Log.e(TAG, "Background location permission is missing.");
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            boolean hasForegroundServiceLocation = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.FOREGROUND_SERVICE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (!hasForegroundServiceLocation) {
                Log.e(TAG, "Foreground service location permission is missing.");
                return false;
            }
        }

        if (!hasFineLocation && !hasCoarseLocation) {
            Log.e(TAG, "Location permissions are missing.");
            return false;
        }

        Log.d(TAG, "All required location permissions are granted.");
        return true;
    }

    private void requestLocationPermissions() {
        Log.d(TAG, "Requesting location permissions...");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        } else { // Pre-Android 10
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private boolean checkBatteryOptimizations() {
        PowerManager powerManager = (PowerManager) getContext().getSystemService(getContext().POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && powerManager != null) {
            boolean isIgnoringOptimizations = powerManager.isIgnoringBatteryOptimizations(getContext().getPackageName());
            if (!isIgnoringOptimizations) {
                Log.d(TAG, "Battery optimization is enabled. Prompting user to disable it.");
                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivityForResult(intent, BATTERY_OPTIMIZATION_REQUEST_CODE);
                return false;
            }
        }
        Log.d(TAG, "Battery optimization is already disabled.");
        return true;
    }

    private void startLocationService() {
        Log.d(TAG, "Starting LocationService...");
        Intent intent = new Intent(getActivity(), LocationService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }
    }

    private void stopLocationService() {
        Log.d(TAG, "Stopping LocationService...");
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().stopService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean permissionsGranted = true;

            // Check each permission result
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }

            if (permissionsGranted) {
                Log.d(TAG, "Location permissions granted.");
                if (checkBatteryOptimizations()) { // Ensure battery optimizations are disabled
                    startLocationService();
                    location_switch.setChecked(true); // Turn the switch ON if permissions are granted
                }
            } else {
                Log.e(TAG, "Location permissions denied.");
                location_switch.setChecked(false); // Ensure the switch stays OFF if permissions are denied

                // Optionally, explain to the user why the permissions are necessary
                showPermissionRationale();
            }
        }
    }


    private void showPermissionRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(TAG, "Showing permission rationale.");
            new AlertDialog.Builder(getContext())
                    .setTitle("Location Permission Required")
                    .setMessage("This app needs location permissions to provide location-based services. Please grant the required permissions.")
                    .setPositiveButton("Grant", (dialog, which) -> requestLocationPermissions())
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Log.d(TAG, "User declined to grant permissions.");
                        location_switch.setChecked(false); // Keep switch OFF
                    })
                    .create()
                    .show();
        } else {
            Log.e(TAG, "User selected 'Don't ask again'. Redirecting to app settings.");
            redirectToAppSettings();
        }
    }

    private void redirectToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        startActivity(intent);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BATTERY_OPTIMIZATION_REQUEST_CODE) {
            if (checkBatteryOptimizations()) {
                Log.d(TAG, "Battery optimization disabled by the user.");
                location_switch.setChecked(true);
                startLocationService();
            } else {
                Log.e(TAG, "Battery optimization still enabled.");
                location_switch.setChecked(false);
            }
        }
    }
    private boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // Android 9 (Pie) and above
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            return locationManager != null && locationManager.isLocationEnabled();
        } else { // Below Android 9
            int mode = Settings.Secure.getInt(
                    getContext().getContentResolver(),
                    Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF
            );
            return mode != Settings.Secure.LOCATION_MODE_OFF;
        }
    }

    private void promptEnableGPS() {
        new AlertDialog.Builder(getContext())
                .setTitle("Enable Location Services")
                .setMessage("Location services are required for this feature. Please enable them.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Log.d(TAG, "User declined to enable GPS.");
                    location_switch.setChecked(false); // Reset the switch
                })
                .create()
                .show();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap; // Store the GoogleMap instance for reuse
        startMapUpdates();
    }

    private void startMapUpdates() {
        Handler handler = new Handler();
        Runnable mapUpdateTask = new Runnable() {
            @Override
            public void run() {
                // Update the map marker
                updateMapMarker();

                // Schedule the next update in 10 seconds
                handler.postDelayed(this, 10000);
            }
        };

        // Start the periodic task immediately
        handler.post(mapUpdateTask);
    }

    private void updateMapMarker() {
        MapUpdate mapUpdate = MapUpdate.getInstance();
        double latitude = mapUpdate.getLatitude();
        double longitude = mapUpdate.getLongitude();

        // Update marker on the map
        LatLng location = new LatLng(latitude, longitude);
        googleMap.clear(); // Clear previous markers
        googleMap.addMarker(new MarkerOptions().position(location).title("Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }


}
