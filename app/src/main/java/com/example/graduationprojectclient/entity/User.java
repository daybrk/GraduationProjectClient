package com.example.graduationprojectclient.entity;

public class User {

    private String email;
    private Long userId;
    private String name;
    private String secondName;
    private String lastName;
    private String password;
    private int accessRights;


    public User(String email, String name, String secondName, String lastName, String password, int accessRights) {
        this.email = email;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.password = password;
        this.accessRights = accessRights;
    }

    public User(String email, String name, String secondName, String password, int accessRights) {
        this.email = email;
        this.name = name;
        this.secondName = secondName;
        this.password = password;
        this.accessRights = accessRights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(int accessRights) {
        this.accessRights = accessRights;
    }
}
