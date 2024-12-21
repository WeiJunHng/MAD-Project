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

    public void fetchUsers(){
        firestoreManager.syncUserTable();
    }

    public void updateUserInFirestore(User user) {
        firestoreManager.executeAction(FirestoreManager.Action.UPDATE, "user", user, context);
    }
}
