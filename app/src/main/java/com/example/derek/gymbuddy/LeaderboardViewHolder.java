package com.example.derek.gymbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

/**
 * View holder for the recycler view
 */
public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsername, tvPoints, tvPosition;
    View mView;

    public LeaderboardViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }//end LeaderboardViewHolder

    public void setUsername(String username){
        tvUsername= mView.findViewById(R.id.usernameRow);
        tvUsername.setText(username);
    }//end setUsername

    public void setPoints(int points){
        String strPoints = "" + points;
        tvPoints = mView.findViewById(R.id.pointsRow);
        tvPoints.setText(strPoints);
    }//end setPoints

    public void setPosition(int position){
        String strPosition = "" + position;
        tvPosition = mView.findViewById(R.id.positionRow);
        tvPosition.setText(strPosition);
    }//end setPosition
}//end class
