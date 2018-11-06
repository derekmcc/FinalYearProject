package com.example.derek.gymbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity {

    //global scope variables and components
    private static final String TAG = "MainActivity";
    private TextView routineDetailsTxt, routineTxt, weightTxt, repsTxt, setsTxt;
    private Spinner routineSpinner, weightSpinner, repsSpinner, setsSpinner;
    private Integer weights[] = {5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,110,120};
    private int sets[] = {1,2,3,4,5};
    private int reps[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

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

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> routineAdapter = ArrayAdapter.createFromResource(this,
                R.array.routines_array, android.R.layout.simple_spinner_item);
        ArrayAdapter <Integer> weightAdapter = new ArrayAdapter<Integer>( this,android.R.layout.simple_spinner_item, weights);
//        ArrayAdapter<Integer> weightAdapter = ArrayAdapter.createFromResource(this,
//                R.array.weight_array, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> repsAdapter = ArrayAdapter.createFromResource(this,
//                R.array.reps_array, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> setsAdapter = ArrayAdapter.createFromResource(this,
//                R.array.sets_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        routineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        repsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        routineSpinner.setAdapter(routineAdapter);
        weightSpinner.setAdapter(weightAdapter);
//        repsSpinner.setAdapter(repsAdapter);
//        setsSpinner.setAdapter(setsAdapter);
    }//end onCreate

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage
}//end class
