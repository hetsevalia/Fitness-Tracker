package org.healthtracker.tracking;

import org.healthtracker.user.User;
import java.sql.*;

/**
 * Represents a mental health tracking activity.
 * This class logs information about user's mood and sleep hours.
 */
public class MentalHealthActivity extends FitnessActivity {
    private int mood;  // Mood rating on a scale of 1 to 5
    private double sleepHours;  // Hours of sleep

    public MentalHealthActivity(String name, int mood, double sleepHours, User user) {
        super(name, user);
        this.mood = mood;
        this.sleepHours = sleepHours;
    }

    /**
     * Tracks the mental health activity by logging mood and sleep details.
     * throws SQLException if there is an error during the database operation.
     */
    @Override
    public void track() throws SQLException {
        String details = "Mood level: " + mood + "/5, Slept " + sleepHours + " hours";
        System.out.println(details);
        saveActivity(details);
    }

    /**
     * Saves the mental health activity details into a user-specific table in the database.
     * Executes the save operation in a new thread to avoid blocking the main application.
     */
    @Override
    protected void saveActivity(String details) {
        new Thread(() -> {
            try {
                ensureTableExists();
                String tableName = "mental_health_" + user.getId() + "_" + user.getUsername();
                String sql = "INSERT INTO " + tableName + " (date, mood, sleep_hours) VALUES (?, ?, ?)";
                try (Connection conn = user.getDatabaseHelper().getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setInt(2, this.mood);
                    pstmt.setDouble(3, this.sleepHours);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error saving mental health activity to the database: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Ensures that a specific table exists for storing mental health activities.
     * Creates the table if it does not exist.
     * throws SQLException if there is an error during table creation.
     */
    private void ensureTableExists() throws SQLException {
        String tableName = "mental_health_" + user.getId() + "_" + user.getUsername();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "activity_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE," +
                "mood INT," +
                "sleep_hours DOUBLE)";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateMentalHealthActivity(int activityId, int newMood, double newSleepHours) throws SQLException {
        String sql = "UPDATE mental_health_" + user.getId() + "_" + user.getUsername() +
                " SET mood = ?, sleep_hours = ? WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newMood);
            pstmt.setDouble(2, newSleepHours);
            pstmt.setInt(3, activityId);
            pstmt.executeUpdate();
        }
    }
    public void deleteMentalHealthActivity(int activityId) throws SQLException {
        String sql = "DELETE FROM mental_health_" + user.getId() + "_" + user.getUsername() + " WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }
}