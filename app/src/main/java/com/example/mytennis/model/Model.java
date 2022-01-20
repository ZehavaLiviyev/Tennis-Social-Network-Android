package com.example.mytennis.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class Model {

    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();

    private Model(){
        for(int i=0;i<10;i++){
            Post s = new Post("des");
            Log.d("TAG","NEW POST ");
            data.add(s);
        }
        Log.d("TAG", String.valueOf(data.size()));
    }

    List<Post> data = new LinkedList<Post>();

    public List<Post> getAll(){
        return data;
    }


//    public LiveData<List<Post>> getAll() {
//        if (postsList.getValue() == null) {
//            //refreshStudentList();
//        }
//        return postsList;
//    }

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
    public void logout() {
        modelFirebase.logout();
    }


//    public interface AddUserListener{
//        void onComplete();
//    }
//    public void addUser(User user, AddStudentListener listener){
//        modelFirebase.addUser( user,  listener);
//    }


}
