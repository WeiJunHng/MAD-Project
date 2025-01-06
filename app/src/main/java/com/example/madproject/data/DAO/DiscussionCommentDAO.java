package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionComment;

import java.util.List;

@Dao
public interface DiscussionCommentDAO extends BaseDAO<DiscussionComment> {

    @Query("SELECT * FROM discussionComment WHERE discussionId = :discussionId")
    List<DiscussionComment> getAllByDiscussionId(String discussionId);

    @Query("SELECT * FROM discussionComment WHERE id = :id")
    DiscussionComment getById(String id);

    @Query("SELECT id FROM discussionComment ORDER BY id DESC LIMIT 1")
    String getLastId();

}

