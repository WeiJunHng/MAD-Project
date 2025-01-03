package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "discussion")
public class Discussion extends Identifiable {
    @NonNull
    private String authorId;
    @NonNull
    private Date timestamp;
    @NonNull
    private String title;
    @NonNull
    private String content;

    public Discussion(@NonNull String id, @NonNull String authorId, @NonNull Date timestamp,
                      @NonNull String title, @NonNull String content) {
        this.id = id;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.title = title;
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
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NonNull String authorId) {
        this.authorId = authorId;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }
}
