package com.example.studentmanagement.exception;

/**
 * Thrown when an authenticated user does not have permission to access a resource.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
