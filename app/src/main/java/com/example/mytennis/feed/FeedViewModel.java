package com.example.mytennis.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.example.mytennis.model.User;
import java.util.List;

public class FeedViewModel extends ViewModel {

    LiveData<List<Post>> data;
    LiveData<List<User>> user_data;

    public FeedViewModel() {
        data = Model.instance.getAllPosts();
        user_data = Model.instance.getAllUsers();
    }

    public LiveData<List<Post>> getPostsData() {
        return data;
    }

    public LiveData<List<User>> getAllUsersData() {
        user_data = Model.instance.getAllUsers();
        return user_data;
    }



}
