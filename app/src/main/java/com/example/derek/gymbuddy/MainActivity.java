package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.derek.gymbuddy.UnityPlayerActivity;
import com.example.derek.gymbuddy.models.Routine;
import com.example.derek.gymbuddy.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private Button plannerBtn, testBtn, repCounterBtn;
   // private FirebaseDatabase mFirebaseDatabase;
    //private FirebaseAuth mAuth;
   // private FirebaseAuth.AuthStateListener mAuthListener;
   // DatabaseReference mDatabase;

   // public String username;
    //public int points = 0;
    //public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //plannerBtn = findViewById(R.id.btnPlanner);
     //   testBtn = findViewById(R.id.btnTest);
       // repCounterBtn = findViewById(R.id.btnRepCounter);

        //String userId = getUid();

        //declare the database reference objects
     //   mAuth = FirebaseAuth.getInstance();
     //   mFirebaseDatabase = FirebaseDatabase.getInstance();
      //  mDatabase = mFirebaseDatabase.getReference();

    }//end onCreate

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button has been clicked
        if (view.getId() == R.id.planner) {
            //log message
            Log.d(TAG,"Going to Planner Activity");

            //go to the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end if
        else if (view.getId() == R.id.gymNearBy) {
            //log message
            Log.d(TAG,"Going to Maps Activity");

            //go to the map activity
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.repCounter) {
            //go to the planner activity
            Intent i = new Intent(this, TestingActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.weightHistory) {
            //go to the weight history activity
            Intent i = new Intent(this, WeightHistoryListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.routinesLookup) {
            //go to the 3D Avatar activity
            Intent intent = new Intent(this, UnityPlayerActivity.class);
            startActivity(intent);
        }//end else if
        else if (view.getId() == R.id.leaderboard) {
            //go to the leaderboard activity
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            startActivity(intent);
        }//end else if
    }//end button listener


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int i = item.getItemId();
//        if (i == R.id.action_logout) {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
}//end class
