package com.example.derek.gymbuddy;

        import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

public class PlannerActivity extends BaseActivity {

    private static final String TAG = "PlannerActivity";
    public static final String EXTRA_ROUTINE_KEY = "routine_key";

    private FloatingActionButton addRoutineBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private List<Routine> routineList = new ArrayList<>();
    private String mRoutineKey;

    private FirebaseRecyclerAdapter <Routine, RoutineViewHolder> mRoutineRVAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        setTitle("Routine Details");

//        final DatabaseReference routineRef = getRef(position);
//        final String routineKey = routineRef.getKey();
        addRoutineBtn = findViewById(R.id.btnAddRoutine);

        //"News" here will reflect what you have called your database in Firebase.->>>>>>>>>>>>>>>CHANGE DB NAME

        mDatabase = FirebaseDatabase.getInstance().getReference().child("user-routiness").child(getUid());
        mDatabase.keepSynced(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        final DatabaseReference routineRef = FirebaseDatabase.getInstance().getReference().child("user-routiness").child(getUid());
        Query routineQuery = routineRef.orderByKey();

        mRecyclerView.hasFixedSize();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions routineOptions = new FirebaseRecyclerOptions.Builder<Routine>().setQuery(routineQuery, Routine.class).build();

        mRoutineRVAdapter = new FirebaseRecyclerAdapter<Routine, RoutineViewHolder>(routineOptions) {
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

                        Intent intent = new Intent(getApplicationContext(), RoutineEditActivity.class);
                        intent.putExtra("routine", routine);
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

        mRecyclerView.setAdapter(mRoutineRVAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }//end onMove

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //get the position of the item to delete
                int deletePosition = viewHolder.getLayoutPosition();
                //pass the position to the delete method
                deleteRoutine(deletePosition);
            }//end onSwipe
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRoutineRVAdapter.startListening();
    }//end onStart

    @Override
    public void onStop() {
        super.onStop();
        mRoutineRVAdapter.stopListening();
    }//end onStop

    public void deleteRoutine(int position) {
        //get a reference to the item to delete
        DatabaseReference itemToRemove = mRoutineRVAdapter.getRef(position);
        //delete item from db
        itemToRemove.removeValue();
        //display toast
        toastMessage("Item Removed Successfully!");
    }//end delete routine method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlannerActivity.this, MainActivity.class));
        finish();
    }//end onBackPressed
}//end class
