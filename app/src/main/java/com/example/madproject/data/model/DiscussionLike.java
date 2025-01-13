package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.Date;

@Entity(tableName = "discussionLike", primaryKeys = {"discussionId", "userId"})
public class DiscussionLike {
    @NonNull
    private String discussionId;
    @NonNull
    private String userId;
    @NonNull
    private Date timestamp;

    public DiscussionLike() {

    }

    public DiscussionLike(@NonNull String discussionId, @NonNull String userId, @NonNull Date timestamp) {
        this.discussionId = discussionId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    @NonNull
    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(@NonNull String discussionId) {
        this.discussionId = discussionId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }
}

