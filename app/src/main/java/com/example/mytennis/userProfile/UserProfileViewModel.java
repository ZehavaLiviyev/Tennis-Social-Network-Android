package com.example.mytennis.userProfile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.example.mytennis.model.User;

import java.util.List;

public class UserProfileViewModel extends ViewModel {
    LiveData<List<Post>> data;

    public UserProfileViewModel() {
        data = Model.instance.getAllPostsForUser(Model.instance.getActiveUser().getEmail());

    }

    public LiveData<List<Post>> getPostsData() {
        data = Model.instance.getAllPostsForUser(Model.instance.getActiveUser().getEmail());
        if(data.getValue()!=null){
            Log.d("getpostdata", String.valueOf(data.getValue().size()));
            for (Post p : data.getValue()){
                Log.d("getpostdata",p.getPostUser());
            }
        }


        return data;
    }



}
