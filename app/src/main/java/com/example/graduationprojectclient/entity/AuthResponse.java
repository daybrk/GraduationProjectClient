package com.example.graduationprojectclient.entity;

public class AuthResponse {

    private String role;
    private String hash;
    private String errorMessage;

    public AuthResponse(String role, String hash, String errorMessage) {
        this.role = role;
        this.hash = hash;
        this.errorMessage = errorMessage;
    }

    public AuthResponse(String role, String hash) {
        this.role = role;
        this.hash = hash;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
