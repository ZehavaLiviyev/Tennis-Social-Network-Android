package com.example.mytennis.connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.User;


public class LoginFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    EditText emailEt, passwordEt;
    Button login_btn, newAccount_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO::
        // search
        // לעבור על הבהרות הפרוייקט


        view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEt = view.findViewById(R.id.frag_login_Email);
        login_btn = view.findViewById(R.id.frag_login_btn);
        passwordEt = view.findViewById(R.id.frag_login_Password);
        newAccount_btn = view.findViewById(R.id.frag_login_newaccount_btn);

        login_btn.setOnClickListener(v -> {
            loginUser();
        });
        newAccount_btn.setOnClickListener(v -> {
            Navigation.findNavController(view)
                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });




        return view;
    }

    private void loginUser() {

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (email.isEmpty()) {
            emailEt.setError("Email is required");
            emailEt.requestFocus();
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


        Model.instance.loginUser(email, password, () -> {
            Model.instance.refreshUserPostsList(Model.instance.getActiveUser().getEmail());
            Navigation.findNavController(view)
                    .navigate(LoginFragmentDirections
                            .actionLoginFragmentToFeedRvFragment(email));
        });

    }


}