package org.healthtracker.tracking;

import org.healthtracker.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents an exercise tracking activity.
 * This class is responsible for logging exercise details like duration and calories burned.
 */
public class ExerciseActivity extends FitnessActivity {
    private int duration;  // Duration of the exercise in minutes
    private int caloriesBurned;  // Calories burned during the exercise

    public ExerciseActivity(String name, int duration, int caloriesBurned, User user) {
        super(name, user);
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    /**
     * Tracks the exercise activity by logging it and saving to the database.
     * throws SQLException if there is an error during the database operation.
     */
    @Override
    public void track() throws SQLException {
        String details = "Performed " + name + " for " + duration + " minutes, burned " + caloriesBurned + " calories.";
        System.out.println(details);
        saveActivity(details);
    }

    /**
     * Saves the exercise activity details into a user-specific table in the database.
     * Executes the save operation in a new thread to avoid blocking the main application.
     */
    @Override
    protected void saveActivity(String details) {
        new Thread(() -> {
            try {
                ensureTableExists();
                String tableName = "exercise_" + user.getId() + "_" + user.getUsername();
                String sql = "INSERT INTO " + tableName + " (date, duration, calories_burned) VALUES (?, ?, ?)";
                try (Connection conn = user.getDatabaseHelper().getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setInt(2, this.duration);
                    pstmt.setInt(3, this.caloriesBurned);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error saving exercise activity to the database: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Ensures that a specific table exists for storing exercise activities.
     * Creates the table if it does not exist.
     * throws SQLException if there is an error during table creation.
     */
    private void ensureTableExists() throws SQLException {
        String tableName = "exercise_" + user.getId() + "_" + user.getUsername();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "activity_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE," +
                "duration INT," +
                "calories_burned INT)";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateExerciseActivity(int activityId, int newDuration, int newCaloriesBurned) throws SQLException {
        String sql = "UPDATE exercise_" + user.getId() + "_" + user.getUsername() +
                " SET duration = ?, calories_burned = ? WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newDuration);
            pstmt.setInt(2, newCaloriesBurned);
            pstmt.setInt(3, activityId);
            pstmt.executeUpdate();
        }
    }
    public void deleteExerciseActivity(int activityId) throws SQLException {
        String sql = "DELETE FROM exercise_" + user.getId() + "_" + user.getUsername() + " WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }
}