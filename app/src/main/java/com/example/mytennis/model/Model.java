package com.example.mytennis.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytennis.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    User activeUser = null;
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    MutableLiveData<List<User>> usersList = new MutableLiveData<List<User>>();
    public Executor executor = Executors.newFixedThreadPool(1);
    MutableLiveData<List<Post>> postsListForUser = new MutableLiveData<List<Post>>();

    MutableLiveData<PostsListLoadingState> postsListLoadingState = new MutableLiveData<PostsListLoadingState>();


    private Model() {
        postsListLoadingState.setValue(PostsListLoadingState.loaded);
    }



    /* ************************************ enum loading posts ************************************** */

    public enum PostsListLoadingState {
        loading,
        loaded
    }

    /* ************************************ init user login+register ******************************** */

    public void checkUserName(String userName, CheckUserNameListener listener) {
        modelFirebase.checkUserName(userName, listener);
    }

    public void registerUser(String email, String password, String fullName, String username, RegisterListener listener) {
        modelFirebase.registerUser(email, password, fullName, username, listener);
    }

    public void loginUser(String email, String password, LoginListener listener) {
        modelFirebase.loginUser(email, password, listener);
        modelFirebase.getUserByUserEmail(email, user -> activeUser = user);
    }

    public void logout(LogoutListener listener) {
        modelFirebase.logout(listener);
    }


    /* ************************************ users *************************************************** */

    public void getCurrentUser(GetCurrentUserListener listener) {
        modelFirebase.getCurrentUser(listener);
    }

    public void deleteUserImage(String proImageName, DeleteUserImageListener listener) {
        modelFirebase.deleteUserImage(proImageName , listener);
    }



    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public void saveUserImage(Bitmap imageBitmap, String imageName, SaveImageUserListener listener) {
        modelFirebase.saveUserImage(imageBitmap, imageName, listener);
    }

    public void saveUpdateUser(String user_name, SaveUserChangeListener listener) {
        modelFirebase.saveUpdateUser(user_name, listener);
    }

    public LiveData<List<User>> getAllUsers() {
        modelFirebase.getAllUsers(list -> usersList.postValue(list));
        return usersList;
    }

    public LiveData<List<Post>> getAllPostsForUser() {
        if (postsListForUser.getValue() == null) {
            refreshUserPostsList();
        }
        return postsListForUser;
    }


    public void refreshUserPostsList() {

        postsListLoadingState.setValue(PostsListLoadingState.loading);

        Long lastUpdateData = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdateData", 0);

        modelFirebase.getAllPosts(lastUpdateData, list ->
                executor.execute(() -> {

                    Long lud = new Long(0);
                    for (Post post : list) {
                        AppLocalDb.db.postDao().insertAll(post);
                        if (lud < post.getUpdateData()) {
                            lud = post.getUpdateData();
                        }
                    }
                    MyApplication.getContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("PostsLastUpdateData", lud)
                            .commit();

                    List<Post> postList = AppLocalDb.db.postDao().getAll();
                    List<Post> userPostList = new ArrayList<Post>();

                    for (Post post : postList) {
                        if (post.getPostUser().equals(activeUser.getEmail())) {
                            Log.d("TAG", "email active user is " + post.getPostUser());
                            userPostList.add(post);
                        }
                    }

                    // sort the posts lists in descending order
                    Collections.sort(userPostList, Collections.reverseOrder());
                    postsListForUser.postValue(userPostList);
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);

                }));
    }


    /* ************************************ posts *************************************************** */

    public void deletePostImage(Post post,DeletePostImageListener listener) {
        modelFirebase.deletePostImage(post, listener, () -> executor.execute(() -> AppLocalDb.db.postDao().delete(post)));
    }


    public void deletePost(Post post, DeletePostListener listener) {
        modelFirebase.deletePost(post, listener, () ->
                executor.execute(() -> {
                    // delete the post from app local db
                    AppLocalDb.db.postDao().delete(post);
                }));
    }

    public LiveData<PostsListLoadingState> getPostsListLoadingState() {
        return postsListLoadingState;
    }

    public LiveData<List<Post>> getAllPosts() {
        if (postsList.getValue() == null) {
            refreshPostsList();
        }
        return postsList;
    }

    public void refreshPostsList() {
        postsListLoadingState.setValue(PostsListLoadingState.loading);

        Long lastUpdateData = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdateData", 0);

        modelFirebase.getAllPosts(lastUpdateData, list ->
                executor.execute(() -> {

                    Long lud = new Long(0);
                    for (Post post : list) {
                        AppLocalDb.db.postDao().insertAll(post);
                        if (lud < post.getUpdateData()) {
                            lud = post.getUpdateData();
                        }
                    }
                    MyApplication.getContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("PostsLastUpdateData", lud)
                            .commit();

                    List<Post> postList = AppLocalDb.db.postDao().getAll();

                    // sort the posts lists in descending order
                    Collections.sort(postList, Collections.reverseOrder());
                    postsList.postValue(postList);
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);

                }));
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


    public Post getPostById(String pId, GetPostByIdListener listener) {
        modelFirebase.getPostById(pId, listener);
        return null;
    }


    /* ************************************ interface *********************************************** */


    public interface DeletePostImageListener {
        void onComplete(Boolean flag);
    }

    public interface DeleteUserImageListener {
        void onComplete(Boolean flag);
    }

    public interface GetCurrentUserListener {
        void onComplete(User user);
    }

    public interface DeletePostListener {
        void onComplete();
    }

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }

    public interface SaveUserChangeListener {
        void onComplete();
    }

    public interface AddPostListener {
        void onComplete();
    }

    public interface SaveImagePostListener {
        void onComplete(String url);
    }

    public interface SaveImageUserListener {
        void onComplete(String url);
    }

    public interface CheckUserNameListener {
        void onComplete(boolean flag);
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
        void onComplete(Long id);

    }


}
