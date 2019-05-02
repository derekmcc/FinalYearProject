package com.example.derek.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.derek.gymbuddy.models.Routine;
import com.example.derek.gymbuddy.models.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to show the Gamification leaderboard
 */
public class LeaderBoardActivity extends BaseActivity {

    private static final String TAG = "LeaderBoardActivity";

    private DatabaseReference databaseReference;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private List<Routine> leaderboardList = new ArrayList<>();
    private FirebaseRecyclerAdapter<UserProfile, LeaderboardViewHolder> mLeaderboardRVAdapter;
    public int pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        setTitle("Leaderboard");
        String email = getEmail();
        String username = getUsernameFromEmail(email);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        mDatabase.keepSynced(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        Query pendingTestsQuery = mDatabase.orderByChild("points");

        mRecyclerView.hasFixedSize();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mLayoutManager = mLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setReverseLayout(true);
        mlayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mlayoutManager);

        FirebaseRecyclerOptions leaderboardOptions = new FirebaseRecyclerOptions.Builder<UserProfile>().setQuery(pendingTestsQuery, UserProfile.class).build();

        mLeaderboardRVAdapter = new FirebaseRecyclerAdapter<UserProfile, LeaderboardViewHolder>(leaderboardOptions) {

            @Override
            protected void onBindViewHolder(final LeaderboardViewHolder holder, int position, final UserProfile model) {
                holder.setUsername(model.getUsername());
                holder.setPoints(model.getPoints());
                holder.setPosition(pos++);
            }

            @Override
            public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_leaderboard_adapter, parent, false);
                return new LeaderboardViewHolder(view);
            }//end RoutineViewHolder
        };
        mRecyclerView.setAdapter(mLeaderboardRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLeaderboardRVAdapter.startListening();
    }//end onStart

    @Override
    public void onStop() {
        super.onStop();
        mLeaderboardRVAdapter.stopListening();
    }//end onStop

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LeaderBoardActivity.this, MainActivity.class));
        finish();
    }//end onBackPressed

    /**
     * Method to handle button clicks on bottom navigation
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button has been clicked
        if(view == findViewById(R.id.planner)){
            //log message
            Log.d(TAG,"Going to Planner Activity");
            finish();
            //go to the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end if planner
        else if (view.getId() == R.id.repCount) {
            finish();
            //go to the planner activity
            Intent i = new Intent(this, RepCounterListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.history) {
            finish();
            //go to the weight history activity
            Intent i = new Intent(this, WeightHistoryListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.lookup) {
            //go to the 3D Avatar activity
            Intent intent = new Intent(this, UnityPlayerActivity.class);
            startActivity(intent);
        }//end else if
        else if (view.getId() == R.id.mainMenu) {
            finish();
            //go to the leaderboard activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }//end else if
    }//end buttonListener method
}
