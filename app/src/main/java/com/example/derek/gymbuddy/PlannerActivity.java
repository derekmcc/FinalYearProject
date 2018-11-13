package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlannerActivity extends AppCompatActivity {

    private static final String TAG = "PlannerActivity";
    private FloatingActionButton addRoutineBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        addRoutineBtn = findViewById(R.id.btnAddRoutine);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myDataset = new String[2];
        myDataset[0] = "Derek";
        myDataset[1] = "Shauna";
        // specify an adapter (see also next example)
        mAdapter = new PlannerAdapterActivity(myDataset);
        mRecyclerView.setAdapter(mAdapter);
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
