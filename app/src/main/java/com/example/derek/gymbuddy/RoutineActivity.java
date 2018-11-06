package com.example.derek.gymbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RoutineActivity extends AppCompatActivity {

    //global scope variables and components
    private static final String TAG = "MainActivity";
    private TextView routineDetailsTxt, routineTxt, weightTxt, repsTxt, setsTxt;
    private Spinner routineSpinner, weightSpinner, repsSpinner, setsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        //assign the id's to the components
        routineDetailsTxt = findViewById(R.id.txtRoutineDetails);
        routineTxt = findViewById(R.id.txtRoutine);
        weightTxt = findViewById(R.id.txtWeight);
        repsTxt = findViewById(R.id.txtReps);
        setsTxt = findViewById(R.id.txtSets);
        routineSpinner = findViewById(R.id.spinnerRoutine);
        weightSpinner = findViewById(R.id.spinnerWeight);
        repsSpinner = findViewById(R.id.spinnerReps);
        setsSpinner = findViewById(R.id.spinnerSets);
    }//end onCreate

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage
}//end class
