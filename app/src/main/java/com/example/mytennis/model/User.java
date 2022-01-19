package com.example.mytennis.model;

import android.widget.ImageView;

public class User {

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
