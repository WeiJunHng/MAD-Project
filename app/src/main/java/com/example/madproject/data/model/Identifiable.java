package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class Identifiable {

    @PrimaryKey
    @NonNull
    protected String id;

    static class IdFormat {
        public static final String USER = "U%06d";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

}