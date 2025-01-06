package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "report")
public class Report extends Identifiable {
    @NonNull
    private String discussionId;
    @NonNull
    private String reporterId;
    @NonNull
    private Date timestamp;
    private String content;

    public Report() {

    }

    @Ignore
    public Report(@NonNull String id, @NonNull String discussionId, @NonNull String reporterId, @NonNull Date timestamp) {
        this.id = id;
        this.discussionId = discussionId;
        this.reporterId = reporterId;
        this.timestamp = timestamp;
        this.content = null;
    }

    public Report(@NonNull String id, @NonNull String discussionId, @NonNull String reporterId, @NonNull Date timestamp, @NonNull String content) {
        this.id = id;
        this.discussionId = discussionId;
        this.reporterId = reporterId;
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
    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(@NonNull String reporterId) {
        this.reporterId = reporterId;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

