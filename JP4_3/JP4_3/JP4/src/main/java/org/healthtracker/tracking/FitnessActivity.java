package org.healthtracker.tracking;

import org.healthtracker.user.User;

import java.sql.SQLException;

/**
 * Abstract class representing a generic fitness activity.
 * This class provides a framework for tracking different types of fitness activities.
 */
public abstract class FitnessActivity {
    protected String name;  // The name of the fitness activity
    protected User user;    // User associated with the fitness activity

    public FitnessActivity(String name, User user) {
        this.name = name;
        this.user = user;
    }

    /**
     * Abstract method to track the specific fitness activity.
     * Implementations should define how the activity is tracked and logged.
     * throws SQLException if there is an error during database operations.
     */
    public abstract void track() throws SQLException;

    /**
     * Abstract method to save activity details to the database.
     * Implementations should specify how the activity details are stored.
     * details Details of the activity to be saved.
     * throws Exception if saving the activity fails.
     */
    protected abstract void saveActivity(String details) throws Exception;
}