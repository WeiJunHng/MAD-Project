package com.example.madproject.ui.precaution;

public class PrecautionItem {
    private String title;
    private String description;
    private int imageResId;

    public PrecautionItem(String title, String description, int imageResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }
}

