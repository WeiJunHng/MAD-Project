package com.example.madproject.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.madproject.data.DAO.BaseDAO;
import com.example.madproject.data.model.ChatGroup;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionComment;
import com.example.madproject.data.model.DiscussionLike;
import com.example.madproject.data.model.EmergencyContact;
import com.example.madproject.data.model.Identifiable;
import com.example.madproject.data.model.Message;
import com.example.madproject.data.model.PeriodCycle;
import com.example.madproject.data.model.PeriodRecord;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirestoreManager {

    public static class Action {
        public static final String INSERT = "INSERT";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
    }

    private final FirebaseFirestore firestore;
    private final AppDatabase database;
    private ExecutorService executorService;
    private static final String TEMP_TABLE_NAME = "tempe";
    private static final Map<String, Class<?>> MODEL_CLASS_MAP = new HashMap<>();
    private static final Map<String, BaseDAO<?>> DAO_MAP = new HashMap<>();
    private static final Map<String, String[]> SQL_EXEC_CODE_MAP = new HashMap<>();

    static {
        // Map tableName to domain class
        MODEL_CLASS_MAP.put("user", User.class);
        MODEL_CLASS_MAP.put("discussion", Discussion.class);
        MODEL_CLASS_MAP.put("discussionComment", DiscussionComment.class);
        MODEL_CLASS_MAP.put("discussionLike", DiscussionLike.class);
        MODEL_CLASS_MAP.put("chatGroup", ChatGroup.class);
        MODEL_CLASS_MAP.put("message", Message.class);
        MODEL_CLASS_MAP.put("report", Report.class);
        MODEL_CLASS_MAP.put("emergencyContact", EmergencyContact.class);
        MODEL_CLASS_MAP.put("periodRecord", PeriodRecord.class);
        MODEL_CLASS_MAP.put("periodCycle", PeriodCycle.class);


        // Map tablename to SQL execution code
        SQL_EXEC_CODE_MAP.put("user", new String[] {
                "CREATE TABLE " + TEMP_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY, " +
                        "firstName TEXT NOT NULL, " +
                        "lastName TEXT NOT NULL, " +
                        "username TEXT NOT NULL, " +
                        "email TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL, " +
                        "profilePic TEXT NOT NULL DEFAULT 'default_profile_pic_url', " +
                        "gender TEXT NOT NULL, " +
                        "age INTEGER NOT NULL, " +
                        "birthday TEXT NOT NULL, " +
                        "contactInfo TEXT UNIQUE, " +
                        "period INTEGER NOT NULL DEFAULT 28);",
                "CREATE TRIGGER check_unique_username " +
                        "BEFORE INSERT ON " + TEMP_TABLE_NAME + " " +
                        "BEGIN " +
                        "    SELECT RAISE(FAIL, 'Username already exists') " +
                        "    WHERE (SELECT COUNT(*) FROM tempe WHERE name = NEW.name) > 0; " +
                        "END;",
                "CREATE TRIGGER check_unique_username_update " +
                        "BEFORE UPDATE ON " + TEMP_TABLE_NAME + " " +
                        "WHEN NEW.name != 'bookworm' AND OLD.name != NEW.name " + // Only check when username changes
                        "BEGIN " +
                        "    SELECT RAISE(FAIL, 'Username already exists') " +
                        "    WHERE (SELECT COUNT(*) FROM tempe WHERE name = NEW.name) > 0; " +
                        "END;"
//                , "CREATE TRIGGER create_user_id " +
//                        "BEFORE INSERT ON " + TEMP_TABLE_NAME + " " +
//                        "FOR EACH ROW " +
//                        "BEGIN " +
//                        "    UPDATE NEW " +
//                        "    SET userId = CONCAT('U', LPAD(COUNT(*)+1, 5, '0')); " +
//                        "END;"
//                ,
//                "UPDATE tempe " +
//                        "SET " +
//                        "    strikeLoginDays = CASE " +
//                        "        WHEN lastLogin = date('now', '-1 day') THEN strikeLoginDays + 1 " +
//                        "        ELSE 1 " +
//                        "    END, " +
//                        "    lastLogin = date('now')"
        });
    }

    public FirestoreManager(AppDatabase database) {
        this.firestore = FirebaseFirestore.getInstance();
        this.database = database;
        // Create a single-threaded executor to run database operations in the background
        this.executorService = Executors.newSingleThreadExecutor();

        // Map tableName to DAO
        DAO_MAP.put("user", database.userDAO());
        DAO_MAP.put("discussion", database.discussionDAO());
        DAO_MAP.put("discussionComment", database.discussionCommentDAO());
        DAO_MAP.put("discussionLike", database.discussionLikeDAO());
        DAO_MAP.put("chatGroup", database.chatGroupDAO());
        DAO_MAP.put("message", database.messageDAO());
        DAO_MAP.put("report", database.reportDAO());
        DAO_MAP.put("emergencyContact", database.emergencyContactDAO());
        DAO_MAP.put("periodCycle", database.periodCycleDAO());
        DAO_MAP.put("periodRecord", database.periodRecordDAO());


    }

    public void clearUserTables() {
        Executor.executeTask(() -> {
            database.userDAO().deleteAll();
        });
    }

    public void clearDiscussionTables() {
        Executor.executeTask(() -> {
            database.discussionDAO().deleteAll();
            database.discussionCommentDAO().deleteAll();
            database.discussionLikeDAO().deleteAll();
        });
    }

    public void onLogin (User user, OnCompleteListener<Boolean> callback) {
        firestore.collection("user").whereEqualTo("userId", user.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                callback.onComplete(Tasks.forResult(true));
            } else {
                Log.e("User Login", "User is not found: " + user.getId());
                callback.onComplete(Tasks.forResult(false));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getModelClass(String tableName) {
        Class<T> modelClass = (Class<T>) MODEL_CLASS_MAP.get(tableName);
        if (modelClass == null) {
            throw new IllegalArgumentException("No Model Class found for table: " + tableName);
        }
        return modelClass;
    }

    @SuppressWarnings("unchecked")
    private <T> BaseDAO<T> getDao(String tableName) {
        BaseDAO<T> dao = (BaseDAO<T>) DAO_MAP.get(tableName);
        if (dao == null) {
            throw new IllegalArgumentException("No DAO found for table: " + tableName);
        }
        return dao;
    }

    public String getDocumentId(Object entity) {
        if (entity instanceof Identifiable) {
            return ((Identifiable) entity).getId();
        }
        return null;
    }

    public String getIdFieldName(String tableName) {
        Class<?> modelClass = getModelClass(tableName);
        if (Identifiable.class.isAssignableFrom(modelClass)) {
            return tableName + "Id"; // Need to implement using class with static final variables
        }
        return "autogenerated";
    }

    private String[] getTemporarySQLExecCode(String tableName) {
        return SQL_EXEC_CODE_MAP.getOrDefault(tableName,null);
    }

    private <T> void insertDataToLocalDatabase(String tableName, T entity) {
        BaseDAO<T> dao = getDao(tableName);
        Executor.executeTask(()->dao.insert(entity));
    }

    private <T> void insertDataToLocalDatabase(String tableName, List<T> entityList) {
        BaseDAO<T> dao = getDao(tableName);
        Executor.executeTask(() -> dao.insertAll(entityList));
    }

    private <T> void deleteDataFromLocalDatabase(String tableName, T entity) {
        BaseDAO<T> dao = getDao(tableName);
        Executor.executeTask(()->dao.delete(entity));
    }

    public <T> LiveData<List<T>> fetchDataByTable(String tableName) {
        Class<T> modelClass = getModelClass(tableName);
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        firestore.collection(tableName).addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.e("FirestoreSync", "Listen failed.", e);
                return;
            }
            if (querySnapshot != null) {
                List<T> dataList = querySnapshot.toObjects(modelClass);

                liveData.setValue(dataList); // Update LiveData for UI

                // Update local database for offline access
                insertDataToLocalDatabase(tableName, dataList);
                Log.d("FirestoreSync", dataList.toString(), e);
            }else{
                Log.d("FirestoreSync", "Syncing failed.", e);
            }
        });
        return liveData;
    }

    public void fetchDataByDocumentId(String tableName,String documentId){
        firestore.collection(tableName).document(documentId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Object currentObject = documentSnapshot.toObject(getModelClass(tableName));
                if (currentObject != null) {
                    Executor.executeTask(() -> insertDataToLocalDatabase(tableName, currentObject));
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync data for table: " + tableName, e));
    }

    public <T> void fetchDataByDataId(String id,String tableName){
        String fieldName = getIdFieldName(tableName);
        firestore.collection(tableName).whereEqualTo(fieldName,id).get().addOnSuccessListener(querySnapshot -> {
            List<T> resultList = querySnapshot.toObjects(getModelClass(tableName));
            Executor.executeTask(() -> insertDataToLocalDatabase(tableName, resultList));
        }).addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync data for table: " + tableName, e));
    }

    private boolean matchedWithReflection(DocumentSnapshot documentSnapshot, Object currentChange) {
        try {
            // Iterate over all declared fields of the class
            for (Field field : currentChange.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Allow access to private fields

                // Get the field name and value from the Room record
                String fieldName = field.getName();
                Object roomValue = field.get(currentChange);

                // Get the corresponding value from the Firestore document
                Object firestoreValue = documentSnapshot.get(fieldName);

                // Compare the values (null-safe)
                if (roomValue != null && !roomValue.equals(firestoreValue) || (roomValue == null && firestoreValue != null)) {
                    return false; // Field values don't match
                }
            }
            return true; // All fields match
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false; // Error during reflection
        }
    }

    private <T> void handleFirestoreInsert(String tableName, T entity) {
        if (entity instanceof Identifiable) {
            String docId = ((Identifiable) entity).getId();
            firestore.collection(tableName).document(docId).set(entity).addOnSuccessListener(aVoid -> {
                Executor.executeTask(() -> insertDataToLocalDatabase(tableName, entity));
                Log.d("Firestore", "Insert successful");
            }).addOnFailureListener(e -> Log.e("Firestore", "Insert failed", e));
            return;
        }
        firestore.collection(tableName).add(entity).addOnSuccessListener(aVoid -> {
            Executor.executeTask(() -> insertDataToLocalDatabase(tableName, entity));
            Log.d("Firestore", "Insert successful");
        }).addOnFailureListener(e -> Log.e("Firestore", "Insert failed", e));
    }

    private <T> void handleFirestoreUpdate(String tableName, T entity) {
        firestore.collection(tableName).get().addOnSuccessListener(querySnapshot -> {
            String matchingId = ((Identifiable) entity).getId();

            if (matchingId == null) {
                for (DocumentSnapshot document : querySnapshot) {
                    if (matchedWithReflection(document, entity)) {
                        matchingId = document.getId();
                        break;
                    }
                }
            }

            if (matchingId == null) {
                return;
            }

            firestore.collection(tableName).document(matchingId).set(entity).addOnSuccessListener(aVoid -> {
                insertDataToLocalDatabase(tableName, entity);
                Log.d("Firestore", "Update successful");
            }).addOnFailureListener(e -> Log.e("Firestore", "Update failed", e));
        });
    }

    private <T> void handleFirestoreDelete(String tableName, T entity){
        if (entity instanceof Identifiable) {
            Identifiable entityNew = (Identifiable) entity;

            firestore.collection(tableName).document(entityNew.getId()).delete().addOnSuccessListener(aVoid -> {
                deleteDataFromLocalDatabase(tableName,entity);
                Log.d("Firestore", "Data deleted successfully");
            }).addOnFailureListener(e -> Log.e("Firestore", "Failed to delete data", e));

            return;
        }

        try {
            // Start building the query
            Query query = firestore.collection(tableName);

            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Make private fields accessible
                try {
                    Object value = field.get(entity); // Get field value
                    if (value != null) {
                        // Add a condition for this field
                        query = query.whereEqualTo(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    Log.e("Firestore", "Error accessing field: " + field.getName(), e);
                }
            }

            // Execute the query and delete matching documents
            query.get().addOnSuccessListener(querySnapshot -> {
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    document.getReference().delete().addOnSuccessListener(aVoid -> {
                        deleteDataFromLocalDatabase(tableName,entity);
                        Log.d("Firestore", "Document deleted: " + document.getId());
                    }).addOnFailureListener(e -> Log.e("Firestore", "Error deleting document", e));
                }
            }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents for deletion", e));
        } catch(SQLiteConstraintException e) {
            Log.e("Deletion", "Unable to delete data.");
        }
    }

    private ContentValues mapDocumentToContentValues(DocumentSnapshot document, Object currentChange) {
        ContentValues values = new ContentValues();
        for (Field field : currentChange.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = document.get(field.getName());
                if (value instanceof String) {
                    values.put(field.getName(), (String) value);
                } else if (value instanceof Integer) {
                    values.put(field.getName(), (Integer) value);
                } else if (value instanceof Boolean) {
                    values.put(field.getName(), (Boolean) value);
                } else if (value instanceof Double) {
                    values.put(field.getName(), (Double) value);
                } else if (value instanceof Long) {
                    values.put(field.getName(), (Long) value);
                }
            } catch (Exception e) {
                Log.e("Reflection", "Failed to map field: " + field.getName(), e);
            }
        }
        return values;
    }

//    private ContentValues mapDocumentToContentValues(DocumentSnapshot document, Object currentChange) {
//        ContentValues values = new ContentValues();
//        for (Field field : currentChange.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            try {
//                Object value = document.get(field.getName());
//                if (value instanceof String) {
//                    values.put(field.getName(), (String) value);
//                } else if (value instanceof Integer) {
//                    values.put(field.getName(), (Integer) value);
//                } else if (value instanceof Boolean) {
//                    values.put(field.getName(), (Boolean) value);
//                } else if (value instanceof Double) {
//                    values.put(field.getName(), (Double) value);
//                } else if (value instanceof Long) {
//                    values.put(field.getName(), (Long) value);
//                }
//            } catch (Exception e) {
//                Log.e("Reflection", "Failed to map field: " + field.getName(), e);
//            }
//        }
//        return values;
//    }

    private void insertIntoTempDatabase(SupportSQLiteDatabase db, ContentValues values) {
        db.beginTransaction();
        try {
            db.insert(TEMP_TABLE_NAME, SQLiteDatabase.CONFLICT_FAIL, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting into " + TEMP_TABLE_NAME + ": " + e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
    }

    public <T> void executeAction(String action, String tableName, T entity, Context context) {
        action = action.toUpperCase();

        switch (action) {
            case Action.INSERT:
                handleFirestoreInsert(tableName, entity);
                return;
            case Action.UPDATE:
                handleFirestoreUpdate(tableName, entity);
                return;
            case Action.DELETE:
                handleFirestoreDelete(tableName, entity);
                return;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        // Configure in-memory SQLite database
//        SupportSQLiteOpenHelper.Configuration config = SupportSQLiteOpenHelper.Configuration.builder(context).name(null) // Temporary in-memory database
//                .callback(new SupportSQLiteOpenHelper.Callback(1) {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        // Create temporary table
//                        String[] createSQL = getTemporarySQLExecCode(tableName);
//                        if (createSQL != null) {
//                            try {
//                                for (String sql : createSQL) {
//                                    db.execSQL(sql);
//                                    Log.d("TempDB", "Executing SQL: " + sql);
//                                }
//                            } catch (Exception e) {
//                                Log.e("TempDB", "Error creating temporary table: " + e.getMessage(), e);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {
//                        // No upgrades needed
//                    }
//                }).build();
//
//        SupportSQLiteDatabase tempDb = new FrameworkSQLiteOpenHelperFactory().create(config).getWritableDatabase();

//        try {
//            // Fetch Firestore data and insert into SQLite temporary db
//            firestore.collection(tableName).get().addOnSuccessListener(querySnapshot -> {
//                if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                    for (DocumentSnapshot document : querySnapshot) {
//                        ContentValues values = mapDocumentToContentValues(document, entity);
//                        insertIntoTempDatabase(tempDb, values);
//                    }
//                } else {
//                    Log.w("Firestore", "No data fetched for table: " + tableName);
//                }
//            }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching collection: " + tableName, e));

            // Don't know need or not, but seems like no need
//            tempDb.execSQL(getInsertStatement(tableName), getVariables(tableName, currentChange));

            // Handle Firestore INSERT or UPDATE


//        } catch (Exception e) {
//            Log.e("Error", "Operation failed: " + e.getMessage(), e);
//        } finally {
//            // Clean up temporary database
//            if (tempDb.isOpen()) {
//                tempDb.execSQL("DROP TABLE IF EXISTS tempe");
//            }
//        }
    }

    public void syncUserTable() {
        LiveData<List<User>> allUserLiveData = fetchDataByTable("user");
    }

    public void syncDiscussionTable() {
        LiveData<List<User>> allDiscussionLiveData = fetchDataByTable("discussion");
        LiveData<List<User>> allDiscussionCommentLiveData = fetchDataByTable("discussionComment");
        LiveData<List<User>> allDiscussionLikeLiveData = fetchDataByTable("discussionLike");
    }

}
