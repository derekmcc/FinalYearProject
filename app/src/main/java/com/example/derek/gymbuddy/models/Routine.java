package com.example.derek.gymbuddy.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Routine {

   // public String uid;
    public String routineName;
    public int weight;
    public int numSets;
    public int numReps;

    public  Routine() {
    }

    public Routine(String routineName, int weight, int numSets, int numReps) {
        //this.uid = uid;
        this.routineName = routineName;
        this.weight = weight;
        this.numSets = numSets;
        this.numReps = numReps;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNumSets() {
        return numSets;
    }

    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }

    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }
}//end class
