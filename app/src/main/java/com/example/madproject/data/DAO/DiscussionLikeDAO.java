package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.DiscussionLike;

@Dao
public interface DiscussionLikeDAO extends BaseDAO<DiscussionLike> {

    @Query("SELECT COUNT(*) FROM discussionLike WHERE discussionId = :discussionId")
    int getDiscussionLikeCount(String discussionId);

    @Query("SELECT COUNT(*) > 0 FROM discussionLike WHERE discussionId = :discussionId AND userId = :userId")
    boolean isLiked(String discussionId, String userId);

    @Query("DELETE FROM user")
    void deleteAll();
}
