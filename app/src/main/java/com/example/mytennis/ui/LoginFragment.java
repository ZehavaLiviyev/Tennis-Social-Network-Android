package com.example.mytennis.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytennis.R;

public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        view.findViewById(R.id.frag_login_btn).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_homeFragment));

        return view;
    }
}