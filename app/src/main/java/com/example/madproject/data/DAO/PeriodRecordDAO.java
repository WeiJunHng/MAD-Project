package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.PeriodRecord;

import java.util.List;

@Dao
public interface PeriodRecordDAO extends BaseDAO<PeriodRecord> {

    @Query("SELECT * FROM periodRecord")
    List<PeriodRecord> getAll();

    @Query("SELECT * FROM periodRecord WHERE userId = :userId AND periodId = :periodId")
    PeriodRecord getByUserIdAndPeriodId(String userId, String periodId);

    @Query("DELETE FROM periodRecord")
    void deleteAll();
}
