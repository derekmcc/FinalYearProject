package com.example.derek.gymbuddy.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }//end default constructor

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }//end constructor
}//end class
