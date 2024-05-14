package org.healthtracker.tracking;

import org.healthtracker.exceptions.DatabaseException;
import org.healthtracker.user.User;
import java.sql.*;

/**
 * Represents a step tracking activity.
 * This class is responsible for logging the number of steps taken by a user.
 */
public class StepActivity extends FitnessActivity {
    private int steps; // Number of steps taken

    public StepActivity(int steps, User user) {
        super("Step Tracking", user);
        this.steps = steps;
    }

    /**
     * Tracks the step activity by logging the number of steps.
     * throws SQLException if there is an error during the database operation.
     */
    @Override
    public void track() throws SQLException {
        String details = "Tracked " + steps + " steps";
        System.out.println(details);
        saveActivity(details);
    }

    /**
     * Saves the step activity details into a user-specific table in the database.
     * Executes the save operation in a new thread to avoid blocking the main application.
     */
    @Override
    protected void saveActivity(String details) {
        new Thread(() -> {
            try {
                ensureTableExists();
                String tableName = "steps_" + user.getId() + "_" + user.getUsername();
                String sql = "INSERT INTO " + tableName + " (date, steps) VALUES (?, ?)";
                try (Connection conn = user.getDatabaseHelper().getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setInt(2, this.steps);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error saving step activity to the database: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Ensures that a specific table exists for storing step activities.
     * Creates the table if it does not exist.
     * throws SQLException if there is an error during table creation.
     */
    private void ensureTableExists() throws SQLException {
        String tableName = "steps_" + user.getId() + "_" + user.getUsername();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "activity_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE," +
                "steps INT)";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateStepActivity(int activityId, int newSteps) throws SQLException {
        String sql = "UPDATE steps_" + user.getId() + "_" + user.getUsername() +
                " SET steps = ? WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newSteps);
            pstmt.setInt(2, activityId);
            pstmt.executeUpdate();
        }
    }
    public void deleteStepActivity(int activityId) throws SQLException {
        String sql = "DELETE FROM steps_" + user.getId() + "_" + user.getUsername() + " WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }

}