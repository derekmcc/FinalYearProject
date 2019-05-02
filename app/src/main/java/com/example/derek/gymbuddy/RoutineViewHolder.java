package com.example.derek.gymbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

/**
 * View holder for the recycler view
 */
public class RoutineViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvWeight, tvReps, tvSets;
    public SwipeLayout swipeLayout;
    public TextView Delete;
    public TextView Edit;
    public TextView Share;

    View mView;
    public RoutineViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }//end routineViewHolder
    public void setRoutine(String routine){
        tvName = (TextView)mView.findViewById(R.id.routineRow);
        tvName.setText(routine);
    }//end setRoutine
    public void setWeight(String weight){
        String strWeight = "" + weight;
        tvWeight = (TextView)mView.findViewById(R.id.weightRow);
        tvWeight.setText(strWeight);
    }//end setWeight
    public void setReps(int reps){
        String strReps = "" + reps;
        tvReps = (TextView)mView.findViewById(R.id.repsRow);
        tvReps.setText(strReps);
    }//end setReps
    public void setSets(int sets){
        String strSets = "" + sets;
        tvSets = (TextView)mView.findViewById(R.id.setRow);
        tvSets.setText(strSets);
    }//end setSets
}//end class
