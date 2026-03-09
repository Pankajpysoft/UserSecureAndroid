package com.usersecure.app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a saved favorite password.
 */
@Entity(tableName = "password_table")
public class PasswordEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    /** The generated or manually entered password */
    public String password;

    /** Length of the password */
    public int length;

    /** Strength rating: Weak, Fair, Good, Strong */
    public String strength;

    /** Timestamp in milliseconds when saved */
    public long timestamp;

    public PasswordEntity(String password, int length, String strength, long timestamp) {
        this.password = password;
        this.length = length;
        this.strength = strength;
        this.timestamp = timestamp;
    }
}
