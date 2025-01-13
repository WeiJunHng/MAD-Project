package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class DbLocation extends Identifiable {
    @PrimaryKey
    @NonNull
    private String id; // Unique ID for the location
    @NonNull
    private String locationName; // Name of the location
    private double longitude; // Longitude of the location
    private double latitude; // Latitude of the location

    public DbLocation() {
    }

    public DbLocation(@NonNull String id, @NonNull String locationName, double longitude, double latitude) {
        this.id = id;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(@NonNull String locationName) {
        this.locationName = locationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
