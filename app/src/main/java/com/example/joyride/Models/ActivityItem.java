package com.example.joyride.Models;

public class ActivityItem {
    private String title;
    private String date;

    public ActivityItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
