package com.example.graduationprojectclient.entity;

public class UserLogIn {

    private String email;
    private String password;

    public UserLogIn(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public String toString() {
        return "email: " + email + ", password: " + password;
    }
}
