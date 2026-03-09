package com.usersecure.app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a saved favorite username.
 */
@Entity(tableName = "username_table")
public class UsernameEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    /** The generated or manually entered username */
    public String username;

    /** Style used (Cool, Funny, Professional, Manual) */
    public String style;

    /** Timestamp in milliseconds when saved */
    public long timestamp;

    public UsernameEntity(String username, String style, long timestamp) {
        this.username = username;
        this.style = style;
        this.timestamp = timestamp;
    }
}
