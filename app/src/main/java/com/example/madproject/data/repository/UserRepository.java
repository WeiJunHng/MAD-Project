package com.example.madproject.data.repository;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.madproject.data.DAO.EmergencyContactDAO;
import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.EmergencyContact;
import com.example.madproject.data.model.User;

import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {

    private static final HashMap<String, User> userMap = new HashMap<>();
    private static User currentUser;
    private final Context context;
    private final UserDAO userDAO; // Local database access
    private final EmergencyContactDAO emergencyContactDAO;
    private final FirestoreManager firestoreManager; // Firestore operations

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.context = context;
        userDAO = database.userDAO();
        emergencyContactDAO = database.emergencyContactDAO();
        firestoreManager = new FirestoreManager(database);
    }

    public User getUserByEmail(String email) {
        return userDAO.getByEmail(email); // Fetch from SQLite database
    }

    public User getUserById(String id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<User> future = executorService.submit(() -> userDAO.getById(id));

        User user = null;
        try {
            user = future.get();
            return user;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            userMap.put(id, user);
        }
    }

    public User getCurrentUser() {
        if(currentUser != null) {
            return currentUser;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("userPreferences", MODE_PRIVATE);
        currentUser = getUserById(sharedPreferences.getString("userId",null));
        return currentUser;
    }

    public String getLastUserId() {
        return userDAO.getLastId();
    }

    public int getUserCount() {
        return userDAO.getUserCount();
    }

    public String createUserId() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            fetchUsers();
            String lastId = userDAO.getLastId();
            if (lastId == null) {
                return String.format("U%06d", 1);
            }
            int numId = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("U%06d", numId);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean emailExist(String email) {
        return userDAO.emailExist(email);
    }

    public boolean usernameExist(String username) {
        return userDAO.usernameExist(username);
    }

    public void fetchUsers() {
        firestoreManager.syncUserTable();
    }

    public void insertUserToFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "user", user, context);
    }

    public void updateUserInFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.UPDATE, "user", user, context);
    }

    public void deleteUserInFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.DELETE, "user", user, context);
    }

    public EmergencyContact getEmergencyContactByUserId(String userId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<EmergencyContact> future = executorService.submit(() -> {
            List<EmergencyContact> contactList = emergencyContactDAO.getByUserId(userId);
            return contactList.get(0);
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEmergencyContactInFirestore(EmergencyContact emergencyContact) {
        firestoreManager.executeAction(FirestoreManager.Action.UPDATE, "emergencyContact", emergencyContact, context);
    }

    public void deleteEmergencyContactInFirestore(EmergencyContact emergencyContact) {
        firestoreManager.executeAction(FirestoreManager.Action.DELETE, "emergencyContact", emergencyContact, context);
    }
}
