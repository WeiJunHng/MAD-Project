package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "discussion")
public class Discussion extends Identifiable {

    private static final String DEFAULT_PICTURE_URL = "defaultPicUrl";

    @NonNull
    private String authorId;
    @NonNull
    @ServerTimestamp
    private Date timestamp;
    @NonNull
    private String pictureUrl;
    @NonNull
    private String content;

    public Discussion() {

    }

    public Discussion(@NonNull String id, @NonNull String authorId, @NonNull Date timestamp, @Nullable String pictureUrl, @NonNull String content) {
        this.id = id;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.pictureUrl = Objects.requireNonNullElse(pictureUrl, DEFAULT_PICTURE_URL);
        this.content = content;
    }

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
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(@NonNull String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }
}
