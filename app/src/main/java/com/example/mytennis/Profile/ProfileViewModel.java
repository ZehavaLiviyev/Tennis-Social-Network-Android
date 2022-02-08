package com.example.mytennis.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.example.mytennis.model.User;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    LiveData<List<Post>> data;

    public ProfileViewModel() {
        data = Model.instance.getAllPostsForUser();
    }

    public LiveData<List<Post>> getPostsData() {
        return data;
    }


}
