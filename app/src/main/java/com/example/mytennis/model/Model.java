package com.example.mytennis.model;

import android.util.Log;

public class Model {

    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    private Model(){}


    public interface RegisterListener{
        void onComplete();
    }
    public interface LoginListener{
        void onComplete();
    }

    public void registerUser(String email, String password, String fullname, String username, RegisterListener listener){
        modelFirebase.registerUser(email, password, fullname, username,listener);
    }

    public void loginUser(String email, String password,LoginListener listener) {
        modelFirebase.loginUser(email, password,listener);
    }


}
