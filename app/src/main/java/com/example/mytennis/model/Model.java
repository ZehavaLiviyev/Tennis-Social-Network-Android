package com.example.mytennis.model;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class Model {

    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();

    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    List<Post> data = new LinkedList<Post>();
    User activeUser = null;
    Integer postId=0;

    public Integer getPostId() {
        return postId;
    }

    private Model() {
        for (int i = 0; i < 10; i++) {
            Post s = new Post("des",i);
            data.add(s);
        }
    }

    public List<Post> getAll() {
        return data;
    }


    public User getActiveUser() {
        return activeUser;
    }

    public void setPostId(int i) {
        postId=i;
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImagePostListener listener) {
        modelFirebase.saveImage(imageBitmap,imageName,listener);
    }

    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post,listener);
    }

    // interfaces

    public interface AddPostListener{
        void onComplete();
    }

    public interface SaveImagePostListener{
        void onComplete(String url);
    }

    public interface RegisterListener {
        void onComplete();

        void onAddUser();
    }

    public interface LoginListener {
        void onComplete();
    }

    public interface LogoutListener {
        void onComplete();
    }

    public interface GetUserByuserName {
        void onComplete(User user);
    }


    public void registerUser(String email, String password, String fullname, String username, RegisterListener listener) {
        modelFirebase.registerUser(email, password, fullname, username, listener);

    }

    public void loginUser(String email, String password, LoginListener listener) {
        modelFirebase.loginUser(email, password, listener);
        modelFirebase.getUserByuserEmail(email, user -> activeUser = user);
    }

    public void logout(LogoutListener listener) {
        modelFirebase.logout(listener);
    }


}
