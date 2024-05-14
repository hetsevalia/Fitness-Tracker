package org.healthtracker.tracking;

import org.healthtracker.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Represents a dietary tracking activity.
 * This class is used for logging food consumption details.
 */
public class DietActivity extends FitnessActivity {
    private String foodItem;  // The food item consumed
    private int calories;     // Calories associated with the food item

    public DietActivity(String foodItem, int calories, User user) {
        super("Diet Tracking", user);
        this.foodItem = foodItem;
        this.calories = calories;
    }

    /**
     * Tracks the dietary activity by logging it and saving to the database.
     * throws SQLException if there is an error during the database operation.
     */
    @Override
    public void track() throws SQLException {
        String details = "Consumed " + foodItem + ": " + calories + " calories";
        System.out.println(details);
        try {
            saveActivity(details);
        } catch (SQLException e) {
            System.err.println("Error saving diet activity to the database: " + e.getMessage());
        }
    }

    /**
     * Saves the dietary activity details into a user-specific table in the database.
     * Ensures that the table exists before saving.
     * details Details of the dietary activity to be saved.
     * throws SQLException if there is an error creating the table or inserting the data.
     */
    @Override
    protected void saveActivity(String details) throws SQLException {
        new Thread(() -> {
            try {
                ensureTableExists();
                String tableName = "diet_" + user.getId() + "_" + user.getUsername();
                String sql = "INSERT INTO " + tableName + " (date, food_item, calories) VALUES (?, ?, ?)";
                try (Connection conn = user.getDatabaseHelper().getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setString(2, this.foodItem);
                    pstmt.setInt(3, this.calories);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                try {
                    throw new SQLException("Error saving diet activity to the database: " + e.getMessage(), e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    /**
     * Ensures that a specific table exists for storing dietary activities.
     * Creates the table if it does not exist.
     * throws SQLException if there is an error during table creation.
     */
    private void ensureTableExists() throws SQLException {
        String tableName = "diet_" + user.getId() + "_" + user.getUsername();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "activity_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE," +
                "food_item VARCHAR(255)," +
                "calories INT)";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateDietActivity(int activityId, String newFoodItem, int newCalories) throws SQLException {
        String sql = "UPDATE diet_" + user.getId() + "_" + user.getUsername() +
                " SET food_item = ?, calories = ? WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newFoodItem);
            pstmt.setInt(2, newCalories);
            pstmt.setInt(3, activityId);
            pstmt.executeUpdate();
        }
    }
    public void deleteDietActivity(int activityId) throws SQLException {
        String sql = "DELETE FROM diet_" + user.getId() + "_" + user.getUsername() + " WHERE activity_id = ?";
        try (Connection conn = user.getDatabaseHelper().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }
}