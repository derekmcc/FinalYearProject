package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button plannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plannerBtn = findViewById(R.id.btnPlanner);
    }//end onCreate

    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button has been clicked
        if (view.getId() == R.id.btnPlanner) {
            //log message
            Log.d(TAG,"Going to Planner Activity");

            //go to the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end if
    }//end button listener

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage
}//end class
