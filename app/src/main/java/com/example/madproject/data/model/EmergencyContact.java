package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emergencyContact")
public class EmergencyContact {

    @PrimaryKey
    @NonNull
    private String userId;

    @NonNull
    private String contactPerson;

    @NonNull
    private String contactInfo;

    // Default constructor
    public EmergencyContact() {
    }

    // Constructor with parameters
    public EmergencyContact(@NonNull String userId, @NonNull String contactPerson, @NonNull String contactInfo) {
        this.userId = userId;
        this.contactPerson = contactPerson;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(@NonNull String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @NonNull
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(@NonNull String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
