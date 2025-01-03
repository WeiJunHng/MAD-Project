package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "message")
public class Message extends Identifiable {
    @NonNull
    private String userId;
    @NonNull
    private String recipientId;
    @NonNull
    private Date timestamp;
    @NonNull
    private String content;

    public Message(@NonNull String id, @NonNull String userId, @NonNull String recipientId,
                   @NonNull Date timestamp, String content) {
        setId(id);  // Set the ID from Identifiable class
        this.userId = userId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
        this.content = content;
    }

    @PrimaryKey
    @NonNull
    public String getId() {
        return super.getId();
    }

    public void setId(@NonNull String id) {
        super.setId(id);
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
    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(@NonNull String recipientId) {
        this.recipientId = recipientId;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }
}
