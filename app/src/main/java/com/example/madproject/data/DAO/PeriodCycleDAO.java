package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.PeriodCycle;

import java.util.List;

@Dao
public interface PeriodCycleDAO extends BaseDAO<PeriodCycle> {

    @Query("SELECT * FROM periodCycle")
    List<PeriodCycle> getAll();

    @Query("SELECT * FROM periodCycle WHERE periodId = :periodId")
    PeriodCycle getByPeriodId(String periodId);

    @Query("DELETE FROM periodCycle")
    void deleteAll();
}
