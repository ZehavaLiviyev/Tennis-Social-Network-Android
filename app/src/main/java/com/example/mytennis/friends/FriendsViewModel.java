package com.example.mytennis.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytennis.model.Model;
import com.example.mytennis.model.User;

import java.util.Collections;
import java.util.List;

public class FriendsViewModel extends ViewModel {

    LiveData<List<User>> data;
    // LiveData<List<User>> matchData;

    public FriendsViewModel() {
        data = Model.instance.getAllUsers();
    }

    public LiveData<List<User>> getDataList() {
        Collections.sort(data.getValue());
        return data;
    }

    public void setDataList(LiveData<List<User>> data) {
        this.data = data;
    }

    /*public LiveData<List<User>> getMatchDataList() {
        return matchData;
    }


    public void findUsingLoop(String search) {
        for(User user: data.getValue()) {
            if (user.getUserName().contains(search)) {
                matchData.getValue().add(user);
            }
        }
    }
*/

}
