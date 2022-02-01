package com.example.mytennis.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytennis.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();


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

                        db.collection(User.COLLECTION_EMAIL_NAME)
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

    public void getUserByuserEmail(String email, Model.GetUserByUserName listener) {

        db.collection(User.COLLECTION_EMAIL_NAME)
                .document(email)
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


    public void addPost(Post post, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();
        db.collection(Post.COLLECTION_NAME)
                .document(String.valueOf(post.getId()))
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void savePostImage(Bitmap imageBitmap, String imageName, Model.SaveImagePostListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("post_images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
                });

    }

    public void changePostId(Model.GetPostIdListener listener) {


        db.collection("PostId")
                .document("id")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.getData()!=null){
                        Map<String, Object> json = new HashMap<String, Object>();
                        int i = 0;
                        String tmp = null;
                        json=documentSnapshot.getData();
                        tmp= String.valueOf(json.get("id"));
                        i=Integer.parseInt(tmp)+1;
                        json.put("id", String.valueOf(i));
                        db.collection("PostId")
                                .document("id")
                                .set(json);
                        listener.onComplete(String.valueOf(i));
                    }
                    else {
                        Map<String, Object> json = new HashMap<String, Object>();
                        json.put("id","1");
                        db.collection("PostId")
                                .document("id")
                                .set(json)
                                .addOnSuccessListener(unused -> listener.onComplete("1"))
                                .addOnFailureListener(ea -> listener.onComplete("1"));
                    }

                })
                .addOnFailureListener(e -> listener.onComplete("1"));
    }


    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    public void getAllPosts(Long lastUpdateData, GetAllPostsListener listener) {

        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateData",new Timestamp(lastUpdateData,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<Post>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Post post = Post.create(doc.getData());
                            if (post != null){
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

}
