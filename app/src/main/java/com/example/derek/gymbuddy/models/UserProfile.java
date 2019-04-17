package com.example.derek.gymbuddy.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserProfile {

    private String username;
    private int points;
    private String uid;

    public  UserProfile () {
    }//end default constructor

    public UserProfile (String uid, String username, int points) {
        this.uid = uid;
        this.username = username;
        this.points = points;
    }//end constructor

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        result.put("points", points);
        return result;
    }//end setData
}
