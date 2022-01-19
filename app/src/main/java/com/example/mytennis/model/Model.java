package com.example.mytennis.model;

public class Model {

    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    private Model(){ }


    public void registerUser(String email, String password, String fullname, String username){
        modelFirebase.registerUser(email,password,fullname,username);
    }


}
