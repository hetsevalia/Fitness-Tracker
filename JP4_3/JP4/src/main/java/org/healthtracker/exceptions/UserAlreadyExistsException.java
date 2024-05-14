package org.healthtracker.exceptions;
/**
 * Custom exception class for handling user creation errors where the user already exists in the system.
 * This exception is thrown during the user registration process if a user with the same username already exists.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}


