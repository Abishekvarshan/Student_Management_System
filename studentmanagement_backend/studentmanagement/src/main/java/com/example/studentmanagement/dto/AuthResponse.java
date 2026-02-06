package com.example.studentmanagement.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String username;
    private String email;
    private String role;

    public AuthResponse(String token, UUID userId, String username, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}