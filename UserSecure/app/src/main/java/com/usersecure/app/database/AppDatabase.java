package com.usersecure.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Singleton Room database for UserSecure app.
 * Contains tables for saved usernames and passwords.
 */
@Database(
    entities = {UsernameEntity.class, PasswordEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "usersecure_db";
    private static volatile AppDatabase INSTANCE;

    // Abstract DAO methods
    public abstract UsernameDao usernameDao();
    public abstract PasswordDao passwordDao();

    /**
     * Get singleton instance of AppDatabase.
     * Thread-safe using double-checked locking pattern.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
