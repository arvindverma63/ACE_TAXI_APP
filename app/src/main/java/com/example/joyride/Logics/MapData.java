package com.example.joyride.Logics;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapData implements OnMapReadyCallback {


    private GoogleMap googleMap;

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
