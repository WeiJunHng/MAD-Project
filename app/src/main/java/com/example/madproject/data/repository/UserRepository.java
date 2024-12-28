package com.example.madproject.data.repository;

import android.content.Context;

import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.User;

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

    public String getLastUserId() {
        return userDAO.getLastUserId();
    }

    public int getUserCount() {
        return userDAO.getUserCount();
    }

    public String createUserId() {
        fetchUsers();
        String lastId = getLastUserId();
        if(lastId == null) {
            return String.format("U%06d", 1);
        }
        int numId = getUserCount();
        return String.format("U%06d", numId+1);
    }

    public boolean emailExist(String email) {
        return userDAO.emailExist(email);
    }

    public boolean usernameExist(String username) {
        return userDAO.usernameExist(username);
    }

    public void fetchUsers(){
        firestoreManager.syncUserTable();
    }

    public void insertUserToFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.INSERT, "user", user, context);
    }

    public void updateUserInFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.UPDATE, "user", user, context);
    }
}
