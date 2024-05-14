package org.healthtracker.user;

import org.healthtracker.db.DatabaseHelper;
import org.healthtracker.exceptions.DatabaseException;
import org.healthtracker.exceptions.UserAlreadyExistsException;

import java.sql.SQLException;
/**
 * Represents a registered user in the system.
 * Extends BaseUser to include user-specific data and functionality such as registration.
 */
public class User extends BaseUser {
    private int id; // Unique identifier for the user
    /**
     * Constructor for User.
     * id The unique identifier for the user.
     * username The username of the user.
     * password The password of the user.
     * dbHelper An instance of DatabaseHelper for database operations.
     */
    public User(int id, String username, String password, DatabaseHelper dbHelper) {
        super(username, password, dbHelper);
        this.id = id;
    }
    /**
     * Registers a new user with the provided details and adds them to the database.
     *  username The username for the new user.
     *  password The password for the new user.
     *  name The name of the new user.
     *  age The age of the new user.
     *  gender The gender of the new user.
     *  weight The weight of the new user.
     *  height The height of the new user.
     *  dbHelper A DatabaseHelper instance for database operations.
     *  return A new instance of User representing the registered user.
     *  throws DatabaseException If there is a problem accessing the database.
     *  throws SQLException If a SQL error occurs.
     */
    public static User register(String username, String password, String name, int age, char gender, double weight, double height, DatabaseHelper dbHelper) throws DatabaseException, SQLException {
        try {
            int userId = dbHelper.addUser(username, password, name, age, gender, weight, height);
            return new User(userId, username, password, dbHelper);
        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage());
            throw e; // Re-throwing DatabaseException for handling at a higher level if necessary
        }
    }
/**
 * Returns the ID of the user.
 * */
    public int getId() {
        return id;
    }
}