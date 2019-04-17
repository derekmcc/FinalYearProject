package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.derek.gymbuddy.models.Routine;
import com.example.derek.gymbuddy.models.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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
}
