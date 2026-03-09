package com.usersecure.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for username favorites in Room database.
 */
@Dao
public interface UsernameDao {

    /** Insert a new username favorite. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UsernameEntity username);

    /** Delete a specific username from favorites. */
    @Delete
    void delete(UsernameEntity username);

    /** Get all saved usernames ordered by most recent first. */
    @Query("SELECT * FROM username_table ORDER BY timestamp DESC")
    LiveData<List<UsernameEntity>> getAllUsernames();

    /** Get all saved usernames ordered by most recent first (non-LiveData, for background ops). */
    @Query("SELECT * FROM username_table ORDER BY timestamp DESC")
    List<UsernameEntity> getAllUsernamesSync();

    /** Check if a username already exists in favorites. */
    @Query("SELECT COUNT(*) FROM username_table WHERE username = :username")
    int existsByUsername(String username);

    /** Get the 5 most recent usernames for the Home screen. */
    @Query("SELECT * FROM username_table ORDER BY timestamp DESC LIMIT 5")
    LiveData<List<UsernameEntity>> getRecentUsernames();

    /** Delete all username favorites. */
    @Query("DELETE FROM username_table")
    void deleteAll();
}
