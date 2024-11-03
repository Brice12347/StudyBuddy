package com.example.studybuddy;

public class User {
    String name, email, username, password;
    private String userID; // Add this field for the unique identifier

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    //added getter and setter for userID for FireBase Authentication
    public String getUserID() {return userID;}
    public void setUserID(String userID) {this.userID = userID;}
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User() {
    }
}