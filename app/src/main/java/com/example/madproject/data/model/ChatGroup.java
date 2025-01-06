package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatGroup", primaryKeys = {"id", "userId"})
public class ChatGroup extends Identifiable {
    @NonNull
    private String userId;
    @NonNull
    private String groupName;

    public ChatGroup() {

    }

    public ChatGroup(@NonNull String id, @NonNull String userId, @NonNull String groupName) {
        this.id = id;
        this.userId = userId;
        this.groupName = groupName;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(@NonNull String groupName) {
        this.groupName = groupName;
    }
}

