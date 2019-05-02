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
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class WearActivity extends WearableActivity implements SensorEventListener{

    public static final String TAG = "WearActivity";
    private TextView mTextView;
    SensorManager mSensorManager;


    float decreasing = 0;
    int rep = 0;
    private boolean yFlag = false;
    private boolean zFlag = false;
    boolean state = false;

    /**
     * Initialize activity
     * @param savedInstanceState Bundle variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        //assign components to variables
        mTextView = (TextView) findViewById(R.id.text);

        //enables always-on
        setAmbientEnabled();

        //object to check if the screen is on
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //assertion
        assert powerManager != null;
        //assign the value of the screen being on true/false
        boolean isScreenOn = powerManager.isInteractive();
        //if the screen is not on
        if (!isScreenOn) {
            //turn on the screen
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }//end if
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //register the local broadcast receiver
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);
    }//end onCreate method

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }//end onStart method

    /**
     * Method to unregister sensor listeners when the app is not in the foreground
     */
    @Override
    protected void onPause() {
        super.onPause();
        //unregister the sensor listener
        mSensorManager.unregisterListener(this);
    }//end onPause method

    /**
     * Method to unregister sensor listeners when the app is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        //unregister the sensor listener
        mSensorManager.unregisterListener(this);
    }//end onStop method

    /**
     * Receive intents from the mobile device
     */
    public class Receiver extends BroadcastReceiver {
        /**
         * This method is called when the receiver receives an intent
         * @param context App context
         * @param intent Intent object
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //String onMessageReceived = "T " + receivedMessageNumber++;
            //mTextView.setText(onMessageReceived);
            //the message being passed from the mobile device
            String in = intent.getStringExtra("message");
            //print the message to the log
            Log.d(TAG,"Message: " + in);
            //if the message is disconnect
            if (in.matches("disconnect")) {
                //unregister the sensor listener
                mSensorManager.unregisterListener((SensorEventListener) context);
                //finish the app
                finish();
                //close the app
                System.exit(0);
            }//end if
        }//end onReceive
    }//on inner class Receiver

    /**
     * Class to send messages to the mobile device
     */
    class SendMessage extends Thread {
        //placeholder for the path of the message (declared in manifest)
        String path;
        //placeholder for the message contents
        String message;

        /**
         * Default constructor
         * @param path Data Path
         * @param message Message contents
         */
        SendMessage(String path, String message) {
            this.path = path;
            this.message = message;
        }//end sendMessage method

        ////

        /**
         * Send the message via the thread. This will send the message to all the currently-connected devices
         */
        public void run() {
            //get all the nodes
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                //block on a task and get the result synchronously
                List<Node> nodes = Tasks.await(nodeListTask);
                //send the message to each device
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(WearActivity.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                    //handle the errors
                    } catch (ExecutionException exception) {
                        Log.e(TAG, "Execution Exception: " + exception);
                    } catch (InterruptedException exception) {
                        Log.e(TAG, "Interrupted Exception: " + exception);
                    }//end catch
                }//end loop
            } catch (ExecutionException exception) {
                Log.e(TAG, "Execution Exception: " + exception);
            } catch (InterruptedException exception) {
                Log.e(TAG, "Interrupted Exception: " + exception);
            }//end catch
        }//end run method
    }//end inner class SendMessage

    /**
     * Method to listen to sensors
     * @param event Data from the sensors
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //if the sensor event is an accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //pass the data to the a method
            getAccelerometer(event);
        }//end if
    }//end onSensorChanged

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
        //if the Y value is in the range of the window, set the Y flag to true
        if (yValue <= -1.3f && yValue >= -3.6f) {
            //set flag to true
            yFlag = true;
        }//end if
        //if the Z value is in the range of the window, set the Z flag to true
        if (zValue >= -1.5f && zValue <= 1.5f) {
            //set flag to true
            zFlag = true;
        }//end if
        //if the xValue is greater than the lowest X axis value
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
        //data path for passing data
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
}//end class
