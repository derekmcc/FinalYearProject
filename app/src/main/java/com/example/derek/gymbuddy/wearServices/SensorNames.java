package com.example.derek.gymbuddy.wearServices;

import android.hardware.Sensor;
import android.util.SparseArray;

public class SensorNames {
    public SparseArray<String> names;

    public SensorNames() {
        names = new SparseArray<String>();

        names.append(0, "Debug Sensor");
        names.append(android.hardware.Sensor.TYPE_ACCELEROMETER, "Accelerometer");
    }

    public String getName(int sensorId) {
        String name = names.get(sensorId);

        if (name == null) {
            name = "Unknown";
        }

        return name;
    }
}
