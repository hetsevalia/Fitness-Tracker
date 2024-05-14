package org.healthtracker.db;

import org.healthtracker.user.User;

import java.sql.*;

public class DatabaseHelper {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthtracker"; // URL of the database
    private static final String USER = "root"; // Database username
    private static final String PASS = "Kalyani@53"; // Database password

    /**
     * Establishes a connection to the database.
     * return A connection to the specified database URL, user, and password.
     * throws SQLException If a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Validates if the provided username and password match a user in the database.
     * username The username to validate.
     * password The password to validate.
     * return True if the credentials match a user in the database, false otherwise.
     */
    public boolean validateUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?"; // SQL query to select the user's password
        try (Connection conn = getConnection(); // Try-with-resources to ensure resources are closed
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username); // Set the username in the query
            ResultSet rs = pstmt.executeQuery(); // Execute the query
            if (rs.next()) {
                return password.equals(rs.getString("password")); // Check if the entered password matches the database
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an SQL exception occurs
            return false;
        }
    }

    public void resetUserData(User user) throws SQLException {
        try (Connection conn = getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);
            try {
                // List of table types to be cleared
                String[] tableTypes = {"diet", "steps", "water_intake", "mental_health", "exercise"};
                for (String type : tableTypes) {
                    String tableName = type + "_" + user.getId() + "_" + user.getUsername();
                    String sql = "DELETE FROM " + tableName;
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.executeUpdate();
                    }
                }
                conn.commit();  // Commit the transaction if all deletions succeed
            } catch (SQLException ex) {
                conn.rollback();  // Rollback if any deletion fails
                throw ex;
            } finally {
                conn.setAutoCommit(true);  // Reset the auto-commit to true
            }
        }
    }


    /**
     * Adds a new user to the database with the provided details.
     * username The username of the new user.
     * password The password of the new user.
     * name The name of the new user.
     * age The age of the new user.
     * gender The gender of the new user.
     * weight The weight of the new user.
     * height The height of the new user.
     * The auto-generated user ID of the newly created user.
     * throws SQLException If inserting the new user fails.
     */
    public int addUser(String username, String password, String name, int age, char gender, double weight, double height) throws SQLException {
        String sql = "INSERT INTO users (username, password, name, age, gender, weight, height) VALUES (?, ?, ?, ?, ?, ?, ?)"; // SQL query to insert a new user
        try (Connection conn = getConnection(); // Try-with-resources to ensure resources are closed
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username); // Set the username
            pstmt.setString(2, password); // Set the password
            pstmt.setString(3, name); // Set the name
            pstmt.setInt(4, age); // Set the age
            pstmt.setString(5, String.valueOf(gender)); // Set the gender as a string
            pstmt.setDouble(6, weight); // Set the weight
            pstmt.setDouble(7, height); // Set the height
            int affectedRows = pstmt.executeUpdate(); // Execute the update and get the number of affected rows
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected."); // Throw an exception if no rows were affected
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) { // Retrieve the generated keys
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated user ID
                } else {
                    throw new SQLException("Creating user failed, no ID obtained."); // Throw an exception if no ID was obtained
                }
            }
        }
    }
}
