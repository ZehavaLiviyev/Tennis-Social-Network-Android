package com.example.mytennis.model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytennis.MyApplication;
import com.example.mytennis.ui.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }


//    public void addUser(User user, AddStudentListener listener){
//        Map<String, Object> json = user.toJson();
//        db.collection(User.COLLECTION_NAME)
//                .document(user.getId())
//                .set(json)
//                .addOnSuccessListener(unused -> listener.onComplete())
//                .addOnFailureListener(e -> listener.onComplete());
//    }

    public void registerUser(String email, String password, String fullname, String username,Model.RegisterListener listener ) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User user = new User(fullname,username,email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(MyApplication.getContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                     }else {
                                        Toast.makeText(MyApplication.getContext(), "Failed to register, try again!!", Toast.LENGTH_LONG).show();
                                    }
                        });
                        listener.onComplete();
                    }else{
                        Toast.makeText(MyApplication.getContext(), "Failed to register, try again!!", Toast.LENGTH_LONG).show();
                    }
                });


    }








    public void loginUser(String email, String password, Model.LoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                listener.onComplete();
            }else {
                Toast.makeText(MyApplication.getContext(), "Failed to login", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout() {
        mAuth.signOut();
    }
}
