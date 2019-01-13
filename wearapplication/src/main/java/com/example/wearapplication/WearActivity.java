package com.example.wearapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;

public class WearActivity extends WearableActivity implements SensorEventListener{

    public static final String TAG = "WearActivity";

    private TextView mTextView;
    SensorManager mSensorManager;
    private Sensor mHeartrateSensor;

    private long lastUpdate;
    private GoogleApiClient mGoogleApiClient;
    ScheduledExecutorService mUpdateScheduler;
    private static final String PATH_SENSOR_DATA = "/sensor_data";
    ScheduledExecutorService scheduler;
    float decreasing = 0;
    private static final String KEY_ACC_X = "acc_x";
    private static final String KEY_ACC_Y = "acc_y";
    private static final String KEY_ACC_Z = "acc_z";
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private String nodeId;
    int receivedMessageNumber = 1;
    //int sentMessageNumber = 1;
   // Button talkButton;
    int rep = 0;
    private boolean yFlag = false;
    private boolean zFlag = false;
    boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        mTextView = (TextView) findViewById(R.id.text);
       // talkButton =  findViewById(R.id.btn);
        // Enables Always-on
        setAmbientEnabled();
//--------------------------For Button------------------------------
//        talkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String onClickMessage = "This is the Wear Activity";
//                mTextView.setText(onClickMessage);
//
//                //Make sure you’re using the same path value//
//                String datapath = "/my_path";
//                new SendMessage(datapath, onClickMessage).start();
//
//            }
//        });
//----------------------------------------------------------------

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Register the local broadcast receiver
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(com.google.android.gms.wearable.Wearable.API)
//                .build();
//        mGoogleApiClient.connect();
//

//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensorManager.registerListener(this,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//        lastUpdate = System.currentTimeMillis();
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
//
//        } else {
//            // Failure! No magnetometer.
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){

        } else {
            // Failure! No magnetometer.
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String onMessageReceived = "I just received a  message from the handheld " + receivedMessageNumber++;
            mTextView.setText(onMessageReceived);
        }
    }

    class SendMessage extends Thread {
        String path;
        String message;

//Constructor///

        SendMessage(String p, String m) {
            path = p;
            message = m;
        }

//Send the message via the thread. This will send the message to all the currently-connected devices//

        public void run() {

            //Get all the nodes//
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                //Block on a task and get the result synchronously//
                List<Node> nodes = Tasks.await(nodeListTask);
                //Send the message to each device//
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(WearActivity.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                    //Handle the errors//
                    } catch (ExecutionException exception) {
                        //TO DO//
                    } catch (InterruptedException exception) {
                        //TO DO//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO//
            } catch (InterruptedException exception) {
//TO DO//
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String s = ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";";
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    /**
     * Method to get the data from the wearables accelerometer
     * @param event Sensor data of the X, Y & Z coordinates
     */
    private void getAccelerometer(SensorEvent event) {
        //assign the accelerometer X,Y,Z data
        float xValue = currentPos(event)[0];
        float yValue = currentPos(event)[1];
        float zValue = currentPos(event)[2];

        //while the x value is decreasing(moving watch down)
        while(xValue < decreasing) {
            //assign x value to decreasing
            decreasing = xValue;
        }//end while

        /**
         * Window for Y value
         *
         * if the Y value is in the range of the window, set the Y flag to true
         */
        if (yValue <= -1.3f && yValue >= -3.6f) {
            //set flag to true
            yFlag = true;
        }//end if

        /**
         * Window for Z value
         *
         * if the Z value is in the range of the window, set the Z flag to true
         */
        if (zValue >= -1.5f && zValue <= 1.5f) {
            //set flag to true
            zFlag = true;
        }//end if

        // the value will be the effect of gravity on the x-axis of the device
        // the value of 9.8 means that the device is perpendicular upwards relative to the x-axis
        // the value of 0 means that the device is parallel relative to the x-axis
        if(xValue > decreasing) {
            //if decreasing is less than 8
            if (decreasing < 8.0f) {
                //if state is false
               if (!state) {
                   //if the X, Y & Z coordinates are within the window range
                    if (xValue > 8.5f && xValue <9.8f && yFlag && zFlag) {
                        //call the onRepIncrease method
                        onRepIncrease();
                        //re-initialize the variables
                        state = !state;
                        decreasing = 0;
                        yFlag = false;
                        zFlag = false;
                    }//end if
                //if state is true
                } else {
                    //if the X value is less than 0
                    if (xValue < 0.0f) {
                        //set state to true
                        state = !state;
                    }//end if
                }//end else
            }//end if
        }//end if
    }//end getAccelerometer method

    /**
     * Method to increment reps and pass this to the mobile device
     */
    public void onRepIncrease() {
        //increment rep
        rep++;
        //create a vibrate object
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        //make the watch vibrate
        Objects.requireNonNull(vibrator).vibrate(100);
        //key for passing data
        String datapath = "/my_path";
        //string value of the number of reps
        String numberOfReps = "" + rep;
        //send the number of reps
        new SendMessage(datapath, numberOfReps).start();
        //set the number of reps on the wearables screen
        mTextView.setText(numberOfReps);
    }//end onRepIncrease

    /**
     * Method to get the current position of the X, Y & Z coordinates from the accelerometer
     * @param event Sensor data of the X, Y & Z coordinates
     * @return Accelerometer coordinates
     */
    private float[] currentPos(SensorEvent event) {
        //float array to hold the accelerometer data
        float sensorData[] = new float[3];
        //assign the X, Y & Z values to the array
        sensorData[0] = event.values[0];
        sensorData[1] = event.values[1];
        sensorData[2] = event.values[2];

        //return the sensor data
        return sensorData;
    }//end currentPosition
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
