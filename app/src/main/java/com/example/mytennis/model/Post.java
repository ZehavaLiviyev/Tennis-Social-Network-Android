package com.example.mytennis.model;

import java.util.HashMap;
import java.util.Map;

public class Post {

    final public static String COLLECTION_NAME = "Posts";

    String postUser;
    String imageUrl;
    String description;
    String id;


    public Post() {
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

        Post post = new Post(description, id, postUser);
        post.setImageUrl(imageUrl);
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
        json.put("imageUrl", imageUrl);
        json.put("description", description);
        json.put("id", id);
        json.put("postUser", postUser);

        return json;
    }
}
