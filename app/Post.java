package com.example.sharingplatform;

public class Post {
    private String content;
    private String imageUrl;

    public Post(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
