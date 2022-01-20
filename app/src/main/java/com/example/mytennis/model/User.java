package com.example.mytennis.model;

import android.widget.ImageView;

import com.google.firebase.Timestamp;

import java.util.Map;

public class User {

    public static final String COLLECTION_NAME = "Users";
    String fullName="";
    String userName="";
   // String password="";
    String email="";
    ImageView profilePicture;


    public User(){}
    public User(String fullName, String userName,String email) {
        this.fullName = fullName;
        this.userName = userName;
     //   this.password = password;
        this.email=email;
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

  //  public String getPassword() {
  //      return password;
   // }

   // public void setPassword(String password) {
   //     this.password = password;
   // }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }







}
