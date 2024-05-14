package org.healthtracker.exceptions;
/**
 * Custom exception class for database access errors.
 * This exception is thrown to indicate that an error occurred during a database operation.
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

