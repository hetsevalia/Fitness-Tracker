package org.healthtracker.user;

import org.healthtracker.exceptions.DatabaseException;
/**
 * Interface defining the contract for authentication capabilities.
 * Implementing this interface requires providing an authentication method
 * that checks if provided username and password credentials are valid.
 */
public interface Authenticatable {
    boolean authenticate(String enteredUsername, String enteredPassword) throws DatabaseException;
}
