package com.example.mytennis.model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytennis.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Map;


public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }


    public void registerUser(String email, String password, String fullname, String username, Model.RegisterListener listener) {

        // authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(fullname, username, email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(MyApplication.getContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MyApplication.getContext(), "Failed to register, try again!!", Toast.LENGTH_LONG).show();
                            }
                        });

                        //add to users collection
                        Map<String, Object> json = user.toJson();
                        db.collection(User.COLLECTION_NAME)
                                .document(user.getUserName())
                                .set(json)
                                .addOnSuccessListener(unused -> listener.onAddUser())
                                .addOnFailureListener(e -> listener.onAddUser());//add to users collection

                        db.collection("Emails")
                                .document(user.getEmail())
                                .set(json)
                                .addOnSuccessListener(unused -> listener.onAddUser())
                                .addOnFailureListener(e -> listener.onAddUser());


                        listener.onComplete();
                    } else {
                        Toast.makeText(MyApplication.getContext(), "Failed to register, try again!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginUser(String email, String password, Model.LoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onComplete();
            } else {
                Toast.makeText(MyApplication.getContext(), "Failed to login", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(Model.LogoutListener listener) {
        mAuth.signOut();
        listener.onComplete();
    }

    public void getUserByuserEmail(String email, Model.GetUserByuserName listener) {

        db.collection("Emails")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            user = User.create(task.getResult().getData());
                        }
                        db.collection(User.COLLECTION_NAME)
                                .document(user.userName)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        User user = null;
                                        if (task.isSuccessful() & task.getResult() != null) {
                                            user = User.create(task.getResult().getData());
                                        }
                                        listener.onComplete(user);
                                    }
                                });
                    }
                });

    }


}
