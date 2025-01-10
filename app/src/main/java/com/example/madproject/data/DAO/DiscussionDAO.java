package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.User;

import java.util.List;

@Dao
public interface DiscussionDAO extends BaseDAO<Discussion> {

    @Query("SELECT * FROM discussion")
    List<Discussion> getAll();

    @Query("SELECT * FROM discussion WHERE id = :id")
    Discussion getById(String id);

    @Query("SELECT id FROM discussion ORDER BY id DESC LIMIT 1")
    String getLastId();

    @Query("DELETE FROM user")
    void deleteAll();

}
