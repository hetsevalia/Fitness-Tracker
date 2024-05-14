package org.healthtracker.tracking;

import org.healthtracker.user.User;
import java.sql.*;

/**
 * Represents a water intake tracking activity.
 * This class logs the amount of water consumed by the user.
 */
public class WaterIntakeActivity extends FitnessActivity {
    private double liters; // Amount of water consumed in liters

    public WaterIntakeActivity(double liters, User user) {
        super("Water Intake", user);
        this.liters = liters;
    }

    /**
     * Tracks the water intake activity by logging the amount of water consumed.
     * throws SQLException if there is an error during the database operation.
     */
    @Override
    public void track() throws SQLException {
        String details = "Consumed " + liters + " liters of water.";
        System.out.println(details);
        saveActivity(details);
    }

    /**
     * Saves the water intake activity details into a user-specific table in the database.
     * Executes the save operation in a new thread to avoid blocking the main application.
     */
    @Override
    protected void saveActivity(String details) {
        new Thread(() -> {
            try {
                ensureTableExists();
                String tableName = "water_intake_" + user.getId() + "_" + user.getUsername();
                String sql = "INSERT INTO " + tableName + " (date, liters) VALUES (?, ?)";
                try (Connection conn = user.getDatabaseHelper().getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setDouble(2, this.liters);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error saving water intake activity to the database: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Ensures that a specific table exists for storing water intake activities.
     * Creates the table if it does not exist.
     * throws SQLException if there is an error during table creation.
     */
    private void ensureTableExists() throws SQLException {
        String tableName = "water_intake_" + user.getId() + "_" + user.getUsername();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "activity_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE," +
                "liters DOUBLE)";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateWaterIntakeActivity(int activityId, double newLiters) throws SQLException {
        String sql = "UPDATE water_intake_" + user.getId() + "_" + user.getUsername() +
                " SET liters = ? WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newLiters);
            pstmt.setInt(2, activityId);
            pstmt.executeUpdate();
        }
    }
    public void deleteWaterIntakeActivity(int activityId) throws SQLException {
        String sql = "DELETE FROM water_intake_" + user.getId() + "_" + user.getUsername() + " WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }
}