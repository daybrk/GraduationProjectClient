package com.example.graduationprojectclient.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Login {

    @NonNull
    @PrimaryKey
    private String email;
    private String password;
    private String role;
    private String token;

    public Login(@NonNull String email, String password, String role, String token) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.token = token;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
