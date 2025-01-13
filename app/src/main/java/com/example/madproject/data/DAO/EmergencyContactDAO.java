package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.EmergencyContact;

import java.util.List;

@Dao
public interface EmergencyContactDAO extends BaseDAO<EmergencyContact> {

    @Query("SELECT * FROM emergencyContact")
    List<EmergencyContact> getAll();

    @Query("SELECT * FROM emergencyContact WHERE userId = :userId")
    List<EmergencyContact> getByUserId(String userId);

    @Query("DELETE FROM emergencyContact")
    void deleteAll();
}
