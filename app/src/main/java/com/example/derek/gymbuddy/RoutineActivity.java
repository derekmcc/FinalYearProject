package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    //global scope variables and components
    private static final String TAG = "RoutineActivity";
    private TextView routineDetailsTxt, routineTxt, weightTxt, repsTxt, setsTxt;
    private Spinner routineSpinner, weightSpinner;
    private NumberPicker setsNumberPicker, repsNumberPicker;
    private Integer weights[] = {5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,110,120};
    private int sets;
    private int reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        //assign the id's to the components
        routineDetailsTxt = findViewById(R.id.txtRoutineDetails);
        routineTxt = findViewById(R.id.txtRoutine);
        weightTxt = findViewById(R.id.txtWeight);
        setsTxt = findViewById(R.id.txtSets);
        repsTxt = findViewById(R.id.txtReps);
        routineSpinner = findViewById(R.id.spinnerRoutine);
        weightSpinner = findViewById(R.id.spinnerWeight);
        setsNumberPicker = (NumberPicker) findViewById(R.id.npSets);
        repsNumberPicker = (NumberPicker) findViewById(R.id.npReps);

        //set the min and max values on the number pickers
        setsNumberPicker.setMinValue(1);
        setsNumberPicker.setMaxValue(5);
        repsNumberPicker.setMinValue(1);
        repsNumberPicker.setMaxValue(20);

        //add listeners to the number pickers
        setsNumberPicker.setOnValueChangedListener(this);
        repsNumberPicker.setOnValueChangedListener(this);

        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> routineAdapter = ArrayAdapter.createFromResource(this,
                R.array.routines_array, android.R.layout.simple_spinner_item);
        ArrayAdapter <Integer> weightAdapter = new ArrayAdapter<Integer>( this,android.R.layout.simple_spinner_item, weights);

        //specify the layout to use when the list of choices appears
        routineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //apply the adapter to the spinner
        routineSpinner.setAdapter(routineAdapter);
        weightSpinner.setAdapter(weightAdapter);
    }//end onCreate

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage

    /**
     * Listener to respond to changes on the number pickers
     * @param picker the number picker been selected
     * @param oldVal value before changing
     * @param newVal value after changing
     */
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker == setsNumberPicker){
            sets = newVal;
        }//end if
        else if (picker == repsNumberPicker) {
            reps = newVal;
        }//end else if
    }//end onValueChange Listener

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //log message
        Log.d(TAG,"Going to Routine Activity");
        Log.d(TAG,"Number of Sets selected : " + sets);
        Log.d(TAG,"Number of Reps selected : " + reps);

        //call the planner activity
        Intent i = new Intent(this, PlannerActivity.class);
        startActivity(i);
    }//end button listener
}//end class
