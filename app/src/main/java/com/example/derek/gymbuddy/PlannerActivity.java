package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlannerActivity extends AppCompatActivity {

    private static final String TAG = "PlannerActivity";
    private FloatingActionButton addRoutineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        addRoutineBtn = findViewById(R.id.btnAddRoutine);

    }//end onCreate method

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage

    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button was clicked
        if (view.getId() == R.id.btnAddRoutine) {
            //log message
            Log.d(TAG,"Going to Routine Activity");
            //call the routine activity
            Intent i = new Intent(this, RoutineActivity.class);
            startActivity(i);
        }//end if
    }//end button listener
}//end class
