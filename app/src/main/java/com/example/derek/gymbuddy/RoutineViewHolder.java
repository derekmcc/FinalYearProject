package com.example.derek.gymbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.derek.gymbuddy.R;

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
    }
    public void setRoutine(String routine){
        tvName = (TextView)mView.findViewById(R.id.routineRow);
        tvName.setText(routine);
    }
    public void setWeight(String weight){
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
