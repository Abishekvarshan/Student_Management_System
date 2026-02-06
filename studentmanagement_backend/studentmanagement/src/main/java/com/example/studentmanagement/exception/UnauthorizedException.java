package com.example.studentmanagement.exception;

/**
 * Thrown when an unauthenticated request tries to access a protected resource.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
