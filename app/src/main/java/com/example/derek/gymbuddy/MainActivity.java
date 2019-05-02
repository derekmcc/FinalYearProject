package com.example.derek.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }//end onCreate


    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button has been clicked
        if (view.getId() == R.id.planner) {
            //log message
            Log.d(TAG,"Going to Planner Activity");

            finish();

            //go to the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end if
        else if (view.getId() == R.id.gymNearBy) {
            //log message
            Log.d(TAG,"Going to Maps Activity");

            finish();
            //go to the map activity
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.repCounter) {
            finish();
            //go to the planner activity
            Intent i = new Intent(this, RepCounterListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.weightHistory) {
            finish();
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
            finish();
            //go to the leaderboard activity
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            startActivity(intent);
        }//end else if
    }//end button listener

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }//end onBackPressed
}//end class
