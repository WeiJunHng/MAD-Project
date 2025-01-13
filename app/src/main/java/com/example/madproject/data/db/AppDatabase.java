package com.example.madproject.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.madproject.data.DAO.ChatGroupDAO;
import com.example.madproject.data.DAO.DiscussionCommentDAO;
import com.example.madproject.data.DAO.DiscussionDAO;
import com.example.madproject.data.DAO.DiscussionLikeDAO;
import com.example.madproject.data.DAO.EmergencyContactDAO;
import com.example.madproject.data.DAO.LocationDAO;
import com.example.madproject.data.DAO.MessageDAO;
import com.example.madproject.data.DAO.PeriodCycleDAO;
import com.example.madproject.data.DAO.PeriodRecordDAO;
import com.example.madproject.data.DAO.ReportDAO;
import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.model.ChatGroup;
import com.example.madproject.data.model.DbLocation;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionComment;
import com.example.madproject.data.model.DiscussionLike;
import com.example.madproject.data.model.EmergencyContact;
import com.example.madproject.data.model.Message;
import com.example.madproject.data.model.PeriodCycle;
import com.example.madproject.data.model.PeriodRecord;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.model.User;

@Database(entities = {
        User.class,
        Discussion.class,
        DiscussionComment.class,
        DiscussionLike.class,
        Report.class,
        ChatGroup.class,
        Message.class,
        EmergencyContact.class,
        PeriodCycle.class,
        PeriodRecord.class,
        DbLocation.class
},version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static volatile AppDatabase INSTANCE;

    public abstract UserDAO userDAO();
    public abstract DiscussionDAO discussionDAO();
    public abstract DiscussionCommentDAO discussionCommentDAO();
    public abstract DiscussionLikeDAO discussionLikeDAO();
    public abstract ReportDAO reportDAO();
    public abstract ChatGroupDAO chatGroupDAO();
    public abstract MessageDAO messageDAO();
    public abstract EmergencyContactDAO emergencyContactDAO();
    public abstract PeriodCycleDAO periodCycleDAO();
    public abstract PeriodRecordDAO periodRecordDAO();
    public abstract LocationDAO locationDAO();



    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
//                    context.deleteDatabase("app_database");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"app_database")
//                            .addMigrations(AppDatabase.MIGRATION_1_2)
//                            .fallbackToDestructiveMigration()
//                            .fallbackToDestructiveMigrationOnDowngrade()
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
                            "id TEXT NOT NULL, " +
                            "firstName TEXT NOT NULL, " +
                            "lastName TEXT NOT NULL, " +
                            "username TEXT NOT NULL, " +
                            "email TEXT NOT NULL, " +
                            "password TEXT NOT NULL, " +
                            "profilePicURL TEXT NOT NULL, " +
                            "gender TEXT NOT NULL, " +
                            "age INTEGER NOT NULL, " +
                            "birthday LONG NOT NULL, " +
                            "contactInfo TEXT NOT NULL, " +
                            "period INTEGER NOT NULL DEFAULT 28, " +
                            "PRIMARY KEY (id)" +
                            ");"
            );

            database.execSQL("DROP TABLE IF EXISTS user;");

            // Create UNIQUE INDEX constraints
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_email ON user_new(email);");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_username ON user_new(username);");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_user_contactInfo ON user_new(contactInfo);");

            database.execSQL("ALTER TABLE user_new RENAME TO user;");

            // Discussion
            database.execSQL("DROP TABLE IF EXISTS discussion;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS discussion (" +
                            "id TEXT NOT NULL, " +
                            "authorId TEXT NOT NULL, " +
                            "timestamp LONG NOT NULL, " +
                            "pictureUrl TEXT NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "PRIMARY KEY (id)" +
                            ");"
            );

            // Comment
            database.execSQL("DROP TABLE IF EXISTS discussionComment;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS discussionComment (" +
                            "id TEXT NOT NULL, " +
                            "discussionId TEXT NOT NULL, " +
                            "commenterId TEXT NOT NULL, " +
                            "timestamp LONG NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "PRIMARY KEY (id)" +
                            ");"
            );

            // Comment
            database.execSQL("DROP TABLE IF EXISTS `discussionLike`;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `discussionLike` (" +
                            "discussionId TEXT NOT NULL, " +
                            "userId TEXT NOT NULL, " +
                            "timestamp LONG NOT NULL, " +
                            "PRIMARY KEY (discussionId, userId)" +
                            ");"
            );

            // Report
            database.execSQL("DROP TABLE IF EXISTS report;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS report (" +
                            "id TEXT NOT NULL, " +
                            "discussionId TEXT NOT NULL, " +
                            "reporterId TEXT NOT NULL, " +
                            "timestamp LONG NOT NULL, " +
                            "content TEXT DEFAULT NULL, " +
                            "PRIMARY KEY (id)" +
                            ");"
            );

            // Chat Group
            database.execSQL("DROP TABLE IF EXISTS `chatGroup`;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `chatGroup` (" +
                            "id TEXT NOT NULL, " +
                            "userId TEXT NOT NULL, " +
                            "groupName TEXT NOT NULL, " +
                            "PRIMARY KEY (id, userId)" +
                            ");"
            );

            // Message
            database.execSQL("DROP TABLE IF EXISTS message;");

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS message (" +
                            "id TEXT NOT NULL, " +
                            "userId TEXT NOT NULL, " +
                            "recipientId TEXT NOT NULL, " +
                            "timestamp LONG NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "PRIMARY KEY (id)" +
                            ");"
            );
        }
    };
}