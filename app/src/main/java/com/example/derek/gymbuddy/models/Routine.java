package com.example.derek.gymbuddy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Routine {

   // public String uid;
    public String routine;
    public int weight;
    public int sets;
    public int reps;
    public String uid;
    public String date;

    public  Routine() {
    }

    public Routine(String uid, String routine, int weight, int sets, int reps, String date) {
        this.uid = uid;
        this.routine = routine;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.date = date;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public void setDate(String date) {
        this.date = date;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("routine", routine);
        result.put("weight", weight);
        result.put("sets", sets);
        result.put("reps", reps);
        result.put("date", date);
        return result;
    }
}//end class
