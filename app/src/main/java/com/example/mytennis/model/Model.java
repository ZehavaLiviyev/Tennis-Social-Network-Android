package com.example.mytennis.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public enum PostsListLoadingState {
        loading,
        loaded
    }

    User activeUser = null;
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    public Executor executor = Executors.newFixedThreadPool(1);
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    MutableLiveData<PostsListLoadingState> postsListLoadingState = new MutableLiveData<PostsListLoadingState>();


    public LiveData<PostsListLoadingState> getPoststListLoadingState() {
        return postsListLoadingState;
    }

    private Model() {
        postsListLoadingState.setValue(PostsListLoadingState.loaded);
    }


    public LiveData<List<Post>> getAllPosts() {
        if (postsList.getValue() == null) {
            refreshStudentList();
        }
        return postsList;
    }

    public void refreshStudentList() {
        postsListLoadingState.setValue(PostsListLoadingState.loading);
        modelFirebase.getAllPosts(new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> list) {

                executor.execute(() -> {

                    postsList.postValue(list);
// 037367603
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);

                });
            }
        });


    }


    public User getActiveUser() {
        return activeUser;
    }


    public void savePostImage(Bitmap imageBitmap, String imageName, SaveImagePostListener listener) {
        modelFirebase.savePostImage(imageBitmap, imageName, listener);
    }

    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, listener);
    }

    public void getPostId(GetPostIdListener listener) {
        modelFirebase.changePostId(listener);
    }

    public void registerUser(String email, String password, String fullName, String username, RegisterListener listener) {
        modelFirebase.registerUser(email, password, fullName, username, listener);
    }

    public void loginUser(String email, String password, LoginListener listener) {
        modelFirebase.loginUser(email, password, listener);
        modelFirebase.getUserByuserEmail(email, user -> activeUser = user);
    }

    public void logout(LogoutListener listener) {
        modelFirebase.logout(listener);
    }

// ************************** interface *********************************************************


    public interface AddPostListener {
        void onComplete();
    }

    public interface SaveImagePostListener {
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

    public interface GetUserByUserName {
        void onComplete(User user);
    }

    public interface GetPostIdListener {
        void onComplete(String id);

    }


}
