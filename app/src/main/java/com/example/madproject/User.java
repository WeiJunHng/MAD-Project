package com.example.madproject;

public class User {
    private String username;
    private int profileImageResId;

    public User(String username, int profileImageResId) {
        this.username = username;
        this.profileImageResId = profileImageResId;
    }

    public String getUsername() {
        return username;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}


