package com.example.mytennis.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.User;


public class IntroFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_intro, container, false);

        Model.instance.executor.execute(() -> {
            Model.instance.getCurrentUser(user -> {
                // User is signed in
                if (user != null) {
                    Model.instance.mainThread.post(() -> {
                        Model.instance.setActiveUser(user);
                        Navigation.findNavController(view).navigate(IntroFragmentDirections.actionIntroFragmentToFeedRvFragment(user.getEmail()));
                    });
                } else {
                    Model.instance.mainThread.post(() -> Navigation.findNavController(view).navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
                    );
                }
            });
        });


        return view;
    }
}