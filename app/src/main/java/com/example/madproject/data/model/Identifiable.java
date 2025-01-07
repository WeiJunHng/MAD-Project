package com.example.madproject.data.model;

import androidx.annotation.NonNull;

public abstract class Identifiable {

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