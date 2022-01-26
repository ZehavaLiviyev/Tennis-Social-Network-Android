package com.example.mytennis.conect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;


public class RegisterFragment extends Fragment {

    EditText fullnameEt, usernameEt, emailEt, passwordEt;
    Button register_btn, account_btn;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        fullnameEt = view.findViewById(R.id.frag_reg_fullname);
        usernameEt = view.findViewById(R.id.frag_reg_username);
        emailEt = view.findViewById(R.id.frag_reg_email);
        passwordEt = view.findViewById(R.id.frag_reg_password);

        register_btn = view.findViewById(R.id.frag_reg_register_btn);
        progressBar = view.findViewById(R.id.frag_reg_progressBar);
        account_btn = view.findViewById(R.id.frag_reg_login_btn);
        progressBar.setVisibility(View.GONE);

        account_btn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        register_btn.setOnClickListener(v -> {
            registerUser(view);
        });


        return view;
    }

    private void registerUser(View view) {
        String fullname = fullnameEt.getText().toString();
        String username = usernameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (fullname.isEmpty()) {
            fullnameEt.setError("Full name is required");
            fullnameEt.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailEt.setError("Email is required");
            emailEt.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            usernameEt.setError("Username is required");
            usernameEt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEt.setError("Password is required");
            passwordEt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEt.setError("Minimum password length should be 6 characters!");
            passwordEt.requestFocus();
            return;
        }

        Model.instance.registerUser(email, password, fullname, username, new Model.RegisterListener() {
            @Override
            public void onComplete() {
                Navigation.findNavController(view).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
            }

            @Override
            public void onAddUser() {
                Log.d("TAG", "onComplete -  register");
            }
        });
    }


}