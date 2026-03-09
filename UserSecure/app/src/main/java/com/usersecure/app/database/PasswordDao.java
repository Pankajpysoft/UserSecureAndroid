package com.usersecure.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for password favorites in Room database.
 */
@Dao
public interface PasswordDao {

    /** Insert a new password favorite. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PasswordEntity password);

    /** Delete a specific password from favorites. */
    @Delete
    void delete(PasswordEntity password);

    /** Get all saved passwords ordered by most recent first. */
    @Query("SELECT * FROM password_table ORDER BY timestamp DESC")
    LiveData<List<PasswordEntity>> getAllPasswords();

    /** Get all saved passwords ordered by most recent first (non-LiveData). */
    @Query("SELECT * FROM password_table ORDER BY timestamp DESC")
    List<PasswordEntity> getAllPasswordsSync();

    /** Check if a password already exists in favorites. */
    @Query("SELECT COUNT(*) FROM password_table WHERE password = :password")
    int existsByPassword(String password);

    /** Get the 5 most recent passwords for the Home screen. */
    @Query("SELECT * FROM password_table ORDER BY timestamp DESC LIMIT 5")
    LiveData<List<PasswordEntity>> getRecentPasswords();

    /** Delete all password favorites. */
    @Query("DELETE FROM password_table")
    void deleteAll();
}
