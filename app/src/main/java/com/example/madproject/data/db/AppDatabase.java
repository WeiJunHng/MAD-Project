package com.example.madproject.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.model.User;

@Database(entities = {User.class},version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public static volatile AppDatabase INSTANCE;

    public abstract UserDAO userDAO();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"app_database")
                            .addMigrations(AppDatabase.MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Perform the migration from version 1 to version 2 here
            // You can use the database object to execute SQL queries or ALTER TABLE statements
            // Example:
            // database.execSQL("ALTER TABLE user ADD COLUMN new_column INTEGER");

            // Enable foreign key support
            database.execSQL("PRAGMA foreign_keys=ON;");

            // User
            database.execSQL("DROP TABLE IF EXISTS user_new;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS user_new (" +
                            "userId TEXT DEFAULT NULL, " +
                            "firstName TEXT NOT NULL, " +
                            "lastName TEXT NOT NULL, " +
                            "username TEXT NOT NULL, " +
                            "email TEXT NOT NULL, " +
                            "password TEXT NOT NULL, " +
                            "profilePicURL TEXT NOT NULL, " +
                            "gender TEXT NOT NULL, " +
                            "age INTEGER NOT NULL, " +
                            "birthday DATE NOT NULL, " +
                            "contactInfo TEXT NOT NULL, " +
                            "period INTEGER NOT NULL DEFAULT 28, " +
                            "PRIMARY KEY (userId)" +
                            ");"
            );

            database.execSQL("DROP TABLE IF EXISTS user;");

            // Create UNIQUE INDEX constraints
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_email ON user_new(email);");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_username ON user_new(username);");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_contactInfo ON user_new(contactInfo);");

            database.execSQL(
                    "CREATE TRIGGER create_user_id " +
                            "BEFORE INSERT ON user " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "    UPDATE NEW " +
                            "    SET userId = CONCAT('U', LPAD(COUNT(*)+1, 5, '0')); " +
                            "END;"
            );

            database.execSQL("ALTER TABLE user_new RENAME TO user;");
        }
    };
}
