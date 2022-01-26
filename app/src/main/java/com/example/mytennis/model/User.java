package com.example.mytennis.model;

import android.widget.ImageView;
import java.util.HashMap;
import java.util.Map;

public class User {

    public static final String COLLECTION_NAME = "Users";
    public static final String COLLECTION_EMAIL_NAME = "Email";
    String fullName = "";
    String userName = "";
    String email = "";
    ImageView profilePicture;


    public User() {
    }

    public User(String fullName, String userName, String email) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
    }

    public static User create(Map<String, Object> json) {
        String fullName = (String) json.get("fullName");
        String userName = (String) json.get("userName");
        String email = (String) json.get("email");
        User user = new User(fullName, userName, email);
        return user;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("fullName", fullName);
        json.put("userName", userName);
        json.put("email", email);
        return json;
    }
}
