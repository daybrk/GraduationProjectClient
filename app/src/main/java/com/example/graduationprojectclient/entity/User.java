package com.example.graduationprojectclient.entity;

public class User {

    private String email;
    private String name;
    private String secondName;
    private String lastName;
    private String password;


    public User(String email, String name, String secondName, String lastName, String password) {
        this.email = email;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String email, String name, String secondName, String password) {
        this.email = email;
        this.name = name;
        this.secondName = secondName;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
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


}
