package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T data);

    // Insert multiple users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<T> data);

    @Update
    void update(T data);

    @Delete
    void delete(T data);
}