package com.example.derek.gymbuddy.events;

import com.example.derek.gymbuddy.wearServices.Sensor;


public class NewSensorEvent {
    private Sensor sensor;

    public NewSensorEvent(Sensor sensor) {
        this.sensor = sensor;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
