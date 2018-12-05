package com.example.wearapplication;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    float x,y,z;
    private static final String KEY_ACC_X = "acc_x";
    private static final String KEY_ACC_Y = "acc_y";
    private static final String KEY_ACC_Z = "acc_z";
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private String nodeId;
    int receivedMessageNumber = 1;
    int sentMessageNumber = 1;
    Button talkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        mTextView = (TextView) findViewById(R.id.text);
        talkButton =  findViewById(R.id.btn);
        // Enables Always-on
        setAmbientEnabled();

        talkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onClickMessage = "This is the Wear Activity";
                mTextView.setText(onClickMessage);

                //Make sure you’re using the same path value//
                String datapath = "/my_path";
                new SendMessage(datapath, onClickMessage).start();

            }
        });
        // Register the local broadcast receiver
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(com.google.android.gms.wearable.Wearable.API)
//                .build();
//        mGoogleApiClient.connect();
//
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

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        x = values[0];
        y = values[1];
        z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (x >= -3 && x <= -2.6 && y >= 5 && y <= 5.5 && z >= 9 && z <= 9.5){
            int rep = 0;
            mTextView.setText("Rep " + (rep++));
            String onClickMessage = "Rep " + rep++;
            mTextView.setText(onClickMessage);

            //Make sure you’re using the same path value//
            String datapath = "/my_path";
            new SendMessage(datapath, onClickMessage).start();
        }
        lastUpdate = actualTime;
        String s = ": X: " + values[0] + "; Y: " + values[1] + "; Z: " + values[2] + ";";
        mTextView.setText(s);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//
//    private void startDataUpdated() {
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//
//        scheduler.scheduleAtFixedRate
//                (new Runnable() {
//                    public void run() {
//                        updateData();
//                    }
//                }, 5, 3, TimeUnit.SECONDS);
//    }
//    private void updateData() {
//        PutDataMapRequest dataMap = PutDataMapRequest.create(PATH_SENSOR_DATA);
//        dataMap.getDataMap().putFloat(KEY_ACC_X, x);
//        dataMap.getDataMap().putFloat(KEY_ACC_Y, y);
//        dataMap.getDataMap().putFloat(KEY_ACC_Z, z);
//
//        PutDataRequest request = dataMap.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mGoogleApiClient, request);
//    }
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register this class as a listener for the orientation and
//        // accelerometer sensors
//        mSensorManager.registerListener(this,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        // unregister listener
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }
//
//    /**
//     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
//     */
//    private void initApi() {
//        mGoogleApiClient = getGoogleApiClient(this);
//        retrieveDeviceNode();
//    }
//
//
//    /**
//     * Returns a GoogleApiClient that can access the Wear API.
//     * @param context
//     * @return A GoogleApiClient that can make calls to the Wear API
//     */
//    private GoogleApiClient getGoogleApiClient(Context context) {
//        return new GoogleApiClient.Builder(context)
//                .addApi(Wearable.API)
//                .build();
//    }
//
//    /**
//     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
//     * multiple connected devices, the first Node ID is returned.
//     */
//    private void retrieveDeviceNode() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
//                NodeApi.GetConnectedNodesResult result =
//                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
//                List<Node> nodes = result.getNodes();
//                if (nodes.size() > 0) {
//                    nodeId = nodes.get(0).getId();
//                }
//                mGoogleApiClient.disconnect();
//            }
//        }).start();
//    }
//    private void sendToast() {
//        GoogleApiClient client = getGoogleApiClient(this);
//        if (nodeId != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
//                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "Hello From Watch", null);
//                    mGoogleApiClient.disconnect();
//                }
//            }).start();
//        }
//    }


}
