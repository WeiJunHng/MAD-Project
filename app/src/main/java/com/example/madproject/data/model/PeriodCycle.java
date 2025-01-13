package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "periodCycle")
public class PeriodCycle {

    @PrimaryKey
    @NonNull
    private String periodId;

    @NonNull
    private String userId;

    @NonNull
    private Date startDate;

    @NonNull
    private Date estimatedEndDate;

    private Date actualEndDate;

    private int period;

    public PeriodCycle() {}

    public PeriodCycle(@NonNull String periodId, @NonNull String userId, @NonNull Date startDate,
                       @NonNull Date estimatedEndDate, Date actualEndDate, int period) {
        this.periodId = periodId;
        this.userId = userId;
        this.startDate = startDate;
        this.estimatedEndDate = estimatedEndDate;
        this.actualEndDate = actualEndDate;
        this.period = period;
    }

    @NonNull
    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(@NonNull String periodId) {
        this.periodId = periodId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    @NonNull
    public Date getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(@NonNull Date estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
