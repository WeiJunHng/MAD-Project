package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.Report;

import java.util.List;

@Dao
public interface ReportDAO extends BaseDAO<Report> {

    @Query("SELECT * FROM report")
    List<Report> getAll();

    @Query("SELECT * FROM report WHERE id = :id")
    Report getById(String id);

    @Query("SELECT * FROM report WHERE discussionId = :discussionId")
    Report getByDiscussion(String discussionId);

    @Query("SELECT id FROM report ORDER BY id DESC LIMIT 1")
    String getLastId();

    @Query("DELETE FROM report")
    void deleteAll();
    
}

