package org.healthtracker.user;

import org.healthtracker.db.DatabaseHelper;
import org.healthtracker.exceptions.DatabaseException;
/**
 * Abstract class that provides a basic implementation of the Authenticatable interface.
 * It includes common attributes and methods shared by different types of users.
 */
abstract class BaseUser implements Authenticatable {
    protected String username; // Username of the user
    protected String password; // Password of the user
    protected DatabaseHelper databaseHelper; // Database helper instance for database operations
    /**
     * Constructor for BaseUser.
     * username The username of the user.
     * password The password of the user.
     * dbHelper An instance of DatabaseHelper for database operations.
     */
    public BaseUser(String username, String password, DatabaseHelper dbHelper) {
        this.username = username;
        this.password = password;
        this.databaseHelper = dbHelper;
    }
    /**
     * Implements the authenticate method from the Authenticatable interface.
     * enteredUsername The username entered by the user.
     * enteredPassword The password entered by the user.
     * return true if the credentials are correct, false otherwise.
     * throws DatabaseException if a database access error occurs.
     */
    @Override
    public boolean authenticate(String enteredUsername, String enteredPassword) throws DatabaseException {
        return databaseHelper.validateUser(enteredUsername, enteredPassword);
    }
    /**
     * Returns the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the database helper associated with this user.
     */
    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
