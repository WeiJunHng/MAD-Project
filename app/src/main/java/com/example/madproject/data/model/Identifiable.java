package com.example.madproject.data.model;

public interface Identifiable {

    class IdFormat {
        public static final String USER = "U%06d";
    }

    String _getId();

}
