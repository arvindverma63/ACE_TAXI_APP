package com.example.joyride.Models;

public class LoginResponse {
    private String token;
    private int userId;
    private String username;

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername(){
        return username;
    }
}
