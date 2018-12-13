package com.example.derek.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.derek.gymbuddy.models.Routine;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_testing);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
private static final String TAG = "PlannerActivity";
    public static final String EXTRA_ROUTINE_KEY = "routine_key";

    private FloatingActionButton addRoutineBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private List<Routine> routineList = new ArrayList<>();
    private String mRoutineKey;

    private FirebaseRecyclerAdapter<Routine, RoutineViewHolder> mPeopleRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        setTitle("Routine Details");

//        final DatabaseReference routineRef = getRef(position);
//        final String routineKey = routineRef.getKey();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("user-routiness").child(getUid());
        mDatabase.keepSynced(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        final DatabaseReference routineRef = FirebaseDatabase.getInstance().getReference().child("user-routiness").child(getUid());
        Query pendingTestsQuery = routineRef.orderByKey();
       // Query pendingTestsQuery = routineRef.orderByKey().equalTo("Arm Curls");
        mRecyclerView.hasFixedSize();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Routine>().setQuery(pendingTestsQuery, Routine.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Routine, RoutineViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final RoutineViewHolder holder, final int position, final Routine model) {
                holder.setRoutine(model.getRoutine());
                holder.setWeight(model.getWeight());
                holder.setSets(model.getSets());
                holder.setReps(model.getReps());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String routine, weight;
                        int reps, sets;
                        routine = model.getRoutine();
                        weight = model.getWeight();
                        reps = model.getReps();
                        sets = model.getSets();

                        Intent intent = new Intent(getApplicationContext(), RepCounterActivity.class);
                        intent.putExtra("weight", weight);
                        intent.putExtra("reps", reps);
                        intent.putExtra("sets", sets);
                        startActivity(intent);
                    }//end onClick
                });
            }//end onBindViewHolder

            @Override
            public RoutineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_planner_adapter, parent, false);

                return new RoutineViewHolder(view);
            }//end RoutineViewHolder
        };
        mRecyclerView.setAdapter(mPeopleRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }//end onStart

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }//end onStop

    public void deleteRoutine(int position) {
        //get a reference to the item to delete
        DatabaseReference itemToRemove = mPeopleRVAdapter.getRef(position);
        //delete item from db
        itemToRemove.removeValue();
        //display toast
        toastMessage("Item Removed Successfully!");
    }//end delete routine method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TestingActivity.this, MainActivity.class));
        finish();
    }//end onBackPressed

}
