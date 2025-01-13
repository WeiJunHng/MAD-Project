package com.example.madproject.data.repository;

import android.content.Context;

import com.example.madproject.data.DAO.LocationDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.DbLocation;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationRepository {

    private final Context context;
    private final FirestoreManager firestoreManager;
    private final LocationDAO locationDAO;

    public LocationRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.context = context;
        firestoreManager = new FirestoreManager(database);
        locationDAO = database.locationDAO();
//        fetchDiscussions();
    }

    public void fetchLocations() {
        firestoreManager.syncLocationTable();
    }

    public String createLocationId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            fetchLocations();
            String lastId = locationDAO.getLastId();
            if (lastId == null) {
                return String.format("L%06d", 1);
            }
            int numId = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("L%06d", numId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DbLocation> getAllLocation() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<DbLocation>> future = executorService.submit(() -> {
//            firestoreManager.clearLocationTables();
            fetchLocations();

            // Sort from latest to oldest
            List<DbLocation> dbLocations = locationDAO.getAll();
//            locations.sort((Location1, Location2) -> Location2.getTimestamp().compareTo(Location1.getTimestamp()));
            return dbLocations;
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public DbLocation getLocationById(String id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<DbLocation> future = executorService.submit(() -> {
//            firestoreManager.clearLocationTables();
            fetchLocations();

            // Sort from latest to oldest
            return locationDAO.getById(id);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertLocationInFirestore(DbLocation dbLocation) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "location", dbLocation, context);
    }

    public void deleteLocationInFirestore(DbLocation dbLocation) {
        firestoreManager.executeAction(FirestoreManager.Action.DELETE, "location", dbLocation, context);
    }

}
