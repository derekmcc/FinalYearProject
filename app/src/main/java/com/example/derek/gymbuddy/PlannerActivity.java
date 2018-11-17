package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.derek.gymbuddy.models.Routine;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class PlannerActivity extends AppCompatActivity {

    private static final String TAG = "PlannerActivity";
    private FloatingActionButton addRoutineBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private List<Routine> routineList = new ArrayList<>();
    private List a = new ArrayList();

    private FirebaseRecyclerAdapter <Routine, PlannerActivity.RoutineViewHolder> mPeopleRVAdapter;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_planner);
//
////        Routine routine = new Routine("Arm Curls",100,4,10);
////        routineList.add(routine);
////        toastMessage(routineList.get(0).routine);
//
//        addRoutineBtn = findViewById(R.id.btnAddRoutine);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        // specify an adapter (see also next example)
//        mAdapter = new PlannerAdapterActivity(routineList);
//        mRecyclerView.setAdapter(mAdapter);
//        routineListPre();
//    }//end onCreate method
//
//    /**
//     * Method to create toast messages
//     * @param message Sentence to be passed
//     */
//    private void toastMessage(String message){
//        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
//    }//end toastMessage
//
//    /**
//     * Method to handle button clicks
//     * @param view Item Clicked
//     */
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
//
//    public void routineListPre(){
//        Routine routine = new Routine("Arm Curls",100,4,10);
//        routineList.add(routine);
//        routine = new Routine("LegCurls",100,4,10);
//        routineList.add(routine);
//        routine = new Routine("Back Curls",50,7,1);
//        routineList.add(routine);
//        routine = new Routine("Tricep Curls",60,3,4);
//        routineList.add(routine);
//        routine = new Routine("Bicep Curls",10,2,7);
//        routineList.add(routine);
//        routine = new Routine("Knee Curls",55,5,12);
//        routineList.add(routine);
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        setTitle("Routine Details");

        addRoutineBtn = findViewById(R.id.btnAddRoutine);
        //"News" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.keepSynced(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("News");
        Query personsQuery = personsRef.orderByKey();

        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Routine>().setQuery(personsQuery, Routine.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Routine, PlannerActivity.RoutineViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(PlannerActivity.RoutineViewHolder holder, final int position, final Routine model) {
                holder.setRoutine(model.getRoutine());
                holder.setWeight(model.getWeight());
                holder.setSets(model.getSets());
                holder.setReps(model.getReps());
               // holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String url = model.getUrl();
//                        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
//                        intent.putExtra("id", url);
//                        startActivity(intent);
//                    }
                //});
            }

            @Override
            public PlannerActivity.RoutineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_planner_adapter, parent, false);

                return new PlannerActivity.RoutineViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(mPeopleRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvWeight, tvReps, tvSets;

        View mView;
        public RoutineViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setRoutine(String routine){
            tvName = (TextView)mView.findViewById(R.id.routineRow);
            tvName.setText(routine);
        }
        public void setWeight(int weight){
            String strWeight = "" + weight;
            tvWeight = (TextView)mView.findViewById(R.id.weightRow);
            tvWeight.setText(strWeight);
        }
        public void setReps(int reps){
            String strReps = "" + reps;
            tvReps = (TextView)mView.findViewById(R.id.repsRow);
            tvReps.setText(strReps);
        }
        public void setSets(int sets){
            String strSets = "" + sets;
            tvSets = (TextView)mView.findViewById(R.id.setRow);
            tvSets.setText(strSets);
        }
    }
}//end class
