package com.example.joyride.Logics;

public class MapUpdate {
    private static MapUpdate instance;
    private double latitude;
    private double longitude;

    private MapUpdate() {
        // Initialize with default values if needed
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public static synchronized MapUpdate getInstance() {
        if (instance == null) {
            instance = new MapUpdate();
        }
        return instance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
