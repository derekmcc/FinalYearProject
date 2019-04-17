package com.example.derek.gymbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsername, tvPoints, tvPosition;
    public SwipeLayout swipeLayout;
    public TextView Delete;
    public TextView Edit;
    public TextView Share;

    View mView;
    public LeaderboardViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }
    public void setUsername(String username){
        tvUsername= (TextView)mView.findViewById(R.id.usernameRow);
        tvUsername.setText(username);
    }
    public void setPoints(int points){
        String strPoints = "" + points;
        tvPoints = (TextView)mView.findViewById(R.id.pointsRow);
        tvPoints.setText(strPoints);
    }

    public void setPosition(int position){
        String strPosition = "" + position;
        tvPosition = (TextView)mView.findViewById(R.id.positionRow);
        tvPosition.setText(strPosition);
    }
}
