package com.example.mytennis.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;


import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    final public static String COLLECTION_NAME = "Posts";

    @PrimaryKey
    @NonNull
    String id;
    String postUser;
    String imageUrl;
    String description;

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    Long updateData = new Long(0);

    public Post() {
    }
    public Long getUpdateData() {
        return updateData;
    }

    public void setUpdateData(Long updateData) {
        this.updateData = updateData;
    }



    public Post(String description, String id, String postUser) {
        this.description = description;
        this.id = id;
        this.postUser = postUser;
    }

    public static Post create(Map<String, Object> json) {
        String description = (String) json.get("description");
        String id = (String) json.get("id");
        String imageUrl = (String) json.get("imageUrl");
        String postUser = (String) json.get("postUser");

        Timestamp ts = (Timestamp) json.get("updateData");
        Long update = ts.getSeconds();

        Post post = new Post(description, id, postUser);
        post.setImageUrl(imageUrl);
        post.setUpdateData(update);
        return post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("updateData", FieldValue.serverTimestamp());
        json.put("imageUrl", imageUrl);
        json.put("description", description);
        json.put("id", id);
        json.put("postUser", postUser);

        return json;
    }
}
