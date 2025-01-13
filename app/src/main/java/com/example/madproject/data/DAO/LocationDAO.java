package com.example.madproject.data.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.madproject.data.model.Location;

import java.util.List;

@Dao
public interface LocationDAO extends BaseDAO<Location> {

    // Retrieve all locations
    @Query("SELECT * FROM location")
    List<Location> getAll();

    // Retrieve a location by ID
    @Query("SELECT * FROM location WHERE id = :id")
    Location getById(String id);

    // Retrieve a location by name
    @Query("SELECT * FROM location WHERE locationName = :locationName")
    Location getByName(String locationName);

    // Retrieve the last added location ID
    @Query("SELECT id FROM location ORDER BY id DESC LIMIT 1")
    String getLastId();

    // Delete all locations
    @Query("DELETE FROM location")
    void deleteAll();
}
