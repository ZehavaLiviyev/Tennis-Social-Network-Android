package com.example.mytennis.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;

import java.util.LinkedList;
import java.util.List;

public class FeedViewModel extends ViewModel {

    //LiveData<List<Post>> data;
    List<Post> data = new LinkedList<Post>();

    public FeedViewModel(){
        data = Model.instance.getAll();
    }
//    public LiveData<List<Post>> getData() {
//        return data;
//    }
    public List<Post> getData() {
        return data;
    }
}
