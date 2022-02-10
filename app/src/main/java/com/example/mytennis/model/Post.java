package com.example.mytennis.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//TODO: חייב אימפורט לפיירבייס?
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements Comparable<Post> {

    final public static String COLLECTION_NAME = "Posts";


    @PrimaryKey
    @NonNull
    Long id;
    String postUser;
    String imageUrl;
    String description;
    Long updateData = new Long(0);

    public Post() {
    }

    public Post(String description, Long id, String postUser) {
        this.description = description;
        this.id = id;
        this.postUser = postUser;
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

    public static Post create(Map<String, Object> json) {

        Long id = (Long) json.get("id");
        String imageUrl = (String) json.get("imageUrl");
        String postUser = (String) json.get("postUser");
        Timestamp ts = (Timestamp) json.get("updateData");
        String description = (String) json.get("description");

        Long update = ts.getSeconds();
        Post post = new Post(description, id, postUser);
        post.setImageUrl(imageUrl);
        post.setUpdateData(update);
        return post;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPostUser() {
        return postUser;
    }

    public Long getUpdateData() {
        return updateData;
    }

    public String getDescription() {
        return description;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUpdateData(Long updateData) {
        this.updateData = updateData;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int compareTo(Post p) {
        return this.getId().compareTo(p.getId());
    }
}
