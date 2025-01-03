package com.example.madproject.data.model;

public abstract class Identifiable {

    protected String id;

    static class IdFormat {
        public static final String USER = "U%06d";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}