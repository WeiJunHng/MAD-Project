package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "report")
public class Report extends Identifiable {

    public static class STATUS {
        public static final String POSTED = "Posted";
        public static final String REMOVED = "Removed";
    }
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String discussionId;
    @NonNull
    private String reporterId;
    @NonNull
    private Date timestamp;
    private String content;
    private String status;

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

    public Report(@NonNull String id, @NonNull String discussionId, @NonNull String reporterId, @NonNull Date timestamp, @NonNull String content, @NonNull String status) {
        this.id = id;
        this.discussionId = discussionId;
        this.reporterId = reporterId;
        this.timestamp = timestamp;
        this.content = content;
        this.status = status;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPosted() {
        return status.equals(STATUS.POSTED);
    }

    public boolean isRemoved() {
        return status.equals(STATUS.REMOVED);
    }
}

