package com.example.madproject.data.repository;

import android.content.Context;

import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {

    private final Context context;
    private final UserDAO userDAO; // Local database access
    private final FirestoreManager firestoreManager; // Firestore operations

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.context = context;
        userDAO = database.userDAO();
        firestoreManager = new FirestoreManager(database);
    }

    public User getUserByEmail(String email) {
        return userDAO.getByEmail(email); // Fetch from SQLite database
    }

    public User getUserById(String id) {
        return userDAO.getById(id);
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
}
