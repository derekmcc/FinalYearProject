package com.example.derek.gymbuddy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.derek.gymbuddy.events.DatabaseHelper;
import com.example.derek.gymbuddy.models.Routine;
import com.example.derek.gymbuddy.models.UserProfile;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestLeaderboardActivity extends BaseActivity {

    DatabaseReference mDatabase;

    TextView tv;
    String value;
    private int points;
    private FirebaseDatabase mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_leaderboard);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //create a reference to the database
        mDatabase = ref.child("leaderboard").child(getUid());
        //get the data from the database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user =  dataSnapshot.getValue(UserProfile.class);
                //tv.setText(" T " + user.getPoints());
                points = user.getPoints();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void jj(View view) {


    }
    private void updatePoints(final int newPoints) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference();
        //final String key = username;
        String userId = getUid();
        String email = getEmail();
        String username = getUsernameFromEmail(email);

        UserProfile  userProfile = new UserProfile(userId, username, newPoints);
        Map<String, Object> postValues = userProfile.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/leaderboard/" + userId, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
