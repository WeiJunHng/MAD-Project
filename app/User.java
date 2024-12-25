package com.example.sharingplatform;

public class User {
    private final String name;
    private final int profileImage;

    public User(String name, int profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public int getProfileImage() {
        return profileImage;
    }
}

