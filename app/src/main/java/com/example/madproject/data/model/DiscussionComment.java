package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@Entity(tableName = "discussionComment")
public class DiscussionComment extends Identifiable {
//    @PrimaryKey
//    @NonNull
//    private String id;
    @NonNull
    private String discussionId;
    @NonNull
    private String commenterId;
    @NonNull
    @ServerTimestamp
    private Date timestamp;
    @NonNull
    private String content;

    public DiscussionComment() {

    }

    public DiscussionComment(@NonNull String id, @NonNull String discussionId, @NonNull String commenterId,
                             @NonNull Date timestamp, @NonNull String content) {
        this.id = id;
        this.discussionId = discussionId;
        this.commenterId = commenterId;
        this.timestamp = timestamp;
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
    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(@NonNull String discussionId) {
        this.discussionId = discussionId;
    }

    @NonNull
    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(@NonNull String commenterId) {
        this.commenterId = commenterId;
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

