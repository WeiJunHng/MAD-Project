package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "periodRecord")
public class PeriodRecord {

    @PrimaryKey
    @NonNull
    private String userId;

    @NonNull
    private String periodId;

    @NonNull
    private Date date;

    private String remark;

    public PeriodRecord() {}

    public PeriodRecord(@NonNull String userId, @NonNull String periodId, @NonNull Date date, String remark) {
        this.userId = userId;
        this.periodId = periodId;
        this.date = date;
        this.remark = remark;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(@NonNull String periodId) {
        this.periodId = periodId;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
