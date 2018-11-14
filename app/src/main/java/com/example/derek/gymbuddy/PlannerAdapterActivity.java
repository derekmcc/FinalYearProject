package com.example.derek.gymbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlannerAdapterActivity extends RecyclerView.Adapter<PlannerAdapterActivity.MyViewHolder>  {

    private String[] mDataset;

    public PlannerAdapterActivity(String[] myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView routine, weight, numReps, numSets;
        public MyViewHolder(View v) {
            super(v);
            routine = (TextView)v.findViewById(R.id.routine);
            weight = (TextView)v.findViewById(R.id.weight);
            numReps = (TextView)v.findViewById(R.id.numReps);
            numSets = (TextView)v.findViewById(R.id.numSets);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_planner_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.routine.setText(mDataset[position]);
        holder.weight.setText(mDataset[position]);
        holder.numReps.setText(mDataset[position]);
        holder.numSets.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
