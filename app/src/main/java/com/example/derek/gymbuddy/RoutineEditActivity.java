package com.example.derek.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.derek.gymbuddy.BaseActivity;
import com.example.derek.gymbuddy.events.DatabaseHelper;
import com.example.derek.gymbuddy.models.Routine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RoutineEditActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {
    //global scope variables and components
    private static final String TAG = "RoutineEditActivity";

    private DatabaseReference mRoutineReference;
    private ValueEventListener mRoutineListener;
    private String routine, weight;
    private int sets, reps;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    DatabaseHelper mDatabaseHelper;
    private TextView routineDetailsTxt, routineTxt, weightTxt, repsTxt, setsTxt;
    private Spinner routineSpinner, weightSpinner;
    private NumberPicker setsNumberPicker, repsNumberPicker;
    private Button addBtn, cancelBtn;
    private String userId, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_edit);
        setTitle("Edit Routine");

        //get routine key from intent
        routine = getIntent().getStringExtra("routine");
        weight = getIntent().getStringExtra("weight");
        reps = getIntent().getIntExtra("reps", 0);
        sets = getIntent().getIntExtra("sets", 0);

        //assign the id's to the components
        routineDetailsTxt = findViewById(R.id.txtRoutineDetails);
        weightTxt = findViewById(R.id.txtWeight);
        setsTxt = findViewById(R.id.txtSets);
        repsTxt = findViewById(R.id.txtReps);
        addBtn = findViewById(R.id.btnAdd);
        cancelBtn = findViewById(R.id.btnCancel);
        routineSpinner = findViewById(R.id.spinnerRoutine);
        weightSpinner = findViewById(R.id.spinnerWeight);
        setsNumberPicker = (NumberPicker) findViewById(R.id.npSets);
        repsNumberPicker = (NumberPicker) findViewById(R.id.npReps);
        userId = getUid();
        userEmail = getEmail();

        //declare the database reference objects
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference();

        //set the min and max values on the number pickers
        setsNumberPicker.setMinValue(1);
        setsNumberPicker.setMaxValue(5);
        repsNumberPicker.setMinValue(1);
        repsNumberPicker.setMaxValue(20);

        //add listeners to the number pickers
        setsNumberPicker.setOnValueChangedListener(this);
        repsNumberPicker.setOnValueChangedListener(this);

        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_array, android.R.layout.simple_spinner_item);
        //specify the layout to use when the list of choices appears
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //apply the adapter to the spinner
        weightSpinner.setAdapter(weightAdapter);

        routineDetailsTxt.setText(routine + " Details");
        int weightPosition = weightAdapter.getPosition(weight);
        weightSpinner.setSelection(weightPosition);
        setsNumberPicker.setValue(sets);
        repsNumberPicker.setValue(reps);
    }//end on create

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

    public void userDetails() {
        String selectedRoutine = routineSpinner.getSelectedItem().toString();
        String selectedWeight = weightSpinner.getSelectedItem().toString();

        if (userId == null) {
            // User is null, error out
            Log.e(TAG, "User " + userId + " is unexpectedly null");
            toastMessage("Error: could not fetch user.");
        }//end if
        else {
            writeNewRoutine(selectedRoutine,selectedWeight,sets, reps);
        }//rnd else
    }//end userDetails
    /**
     * Method to handle button clicks
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        if (view.getId() == R.id.btnAdd) {
            String selectedWeight =  weightSpinner.getSelectedItem().toString();

            //conditional statement to set the default values to 1
            if (reps == 0) {
                reps = 1;
            }//end if
            if (sets == 0) {
                sets = 1;
            }//end if

            //add the new routine to the db
            writeNewRoutine(routine,selectedWeight,sets, reps);
            //show the loading dialog
            showProgressDialog();

            //create an intent to go back to the Planner page
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.btnCancel) {
            //call the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end else if
    }//end button listener

    private void writeNewRoutine(final String routineName, final String weight, final int numSets, final int numReps) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);

        final String key = routineName;
        Routine routine = new Routine(userId, routineName, weight, numSets, numReps, formattedDate);
        Map<String, Object> postValues = routine.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-routiness/" + userId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);

        String data = routineName + ", " + weight + ", " + formattedDate;

        //write to file DB--------------------------------------------------
        //writeToFile(data);
        mDatabaseHelper = new DatabaseHelper(this,routineName);
        mDatabaseHelper.addData(weight, formattedDate);
        //---------------------------------------------------------------------------
        //display toast message
        toastMessage(routineName);

        // String key = mDatabase.child("routines").push().getKey();
//        final String key = routineName;
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("user-routiness").child(getUid()).hasChild(key)) {
//                    toastMessage("Routine Already Exists, \nEdit Routine Instead");
//                }//end if
//                else {
//                    Routine routine = new Routine(userId, routineName, weight, numSets, numReps, formattedDate);
//                    Map<String, Object> postValues = routine.toMap();
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    childUpdates.put("/user-routiness/" + userId + "/" + key, postValues);
//                    mDatabase.updateChildren(childUpdates);
//                    //display toast message
//                    toastMessage("Routine Successfully Updated");
//                }//end else
//            }//end onDataChange
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }//end onCancelled
//        });
    }
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("routine.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }//end catch
    }//end writeToFile method
}//end class
