package com.example.wearapplication;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageRecieiverService extends WearableListenerService {

    public static final String TAG = "MessageRecieiverService";
    private static final String CONNECT = "/connect";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "Message Recieved");
        if (messageEvent.getPath().equals("/my_path")) {
            final String message = new String(messageEvent.getData());
            Log.d(TAG, "Message: " + message);
            //Broadcast the received data layer messages//
            Intent messageIntent = new Intent(getApplicationContext(), WearActivity.class);
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            startActivity(messageIntent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            //startActivity(messageIntent);//Start activity instead of passing messages/0ther 3 lines pass message
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }//end on messageReceive
}//end class
