package com.example.mytennis.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;

public class ProfileFragment extends Fragment {

    Button logoutbtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutbtn = view.findViewById(R.id.frag_profile_logout_btn);

        logoutbtn.setOnClickListener(v -> {
           logout(view);
        });

        return view;
    }

    // TODO blocking the previous page (when we go back after the logout action)

    private void logout(View view) {
        Model.instance.logout();
        Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment());
    }
}