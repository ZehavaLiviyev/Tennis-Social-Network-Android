package com.example.mytennis.model;

import java.util.HashMap;
import java.util.Map;

public class Post {

    final public static String COLLECTION_NAME ="Posts";

    String imageUrl;
    String description;
    Integer id;


    public Post() {
    }

    public Post(String description, Integer id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

        return json;
    }
}
