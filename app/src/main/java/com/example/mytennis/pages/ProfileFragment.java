package com.example.mytennis.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.User;

public class ProfileFragment extends Fragment {

    User user;
    TextView usernameTv;
    ImageView photoImv;
    Button logout_btn;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout_btn = view.findViewById(R.id.frag_profile_logout_btn);
        usernameTv = view.findViewById(R.id.frag_profile_usern_tv);
        photoImv = view.findViewById(R.id.frag_profile_imv);
        user = Model.instance.getActiveUser();
        usernameTv.setText(user.getUserName());

        logout_btn.setOnClickListener(v -> {
            logout();
        });

        return view;
    }


    private void logout() {
        Model.instance.logout(() -> {
            Navigation.findNavController(this.view).navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment());
        });

    }
}