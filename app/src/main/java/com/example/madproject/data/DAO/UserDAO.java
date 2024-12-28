package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.User;

import java.util.List;

@Dao
public interface UserDAO extends BaseDAO<User>{

    // Query all Users
    @Query("SELECT * FROM user")
    List<User> getAll();

    // Query a User by userId
    @Query("SELECT * FROM user WHERE userId = :userId")
    User getById(String userId);

    // Query a User by email
    @Query("SELECT * FROM user WHERE email = :email")
    User getByEmail(String email);

    // Query a User username
    @Query("SELECT * FROM user WHERE username = :username")
    User getByUsername(String username);

    @Query("SELECT userId FROM user ORDER BY userId DESC LIMIT 1")
    String getLastUserId();

    @Query("SELECT COUNT(*) FROM user")
    int getUserCount();

    // Check if username exists
    @Query("SELECT COUNT(*) > 0 FROM user WHERE username = :username")
    boolean usernameExist(String username);

    // Check if username exists
    @Query("SELECT COUNT(*) > 0 FROM user WHERE email = :email")
    boolean emailExist(String email);

    @Query("DELETE FROM user")
    void deleteAll();

}
