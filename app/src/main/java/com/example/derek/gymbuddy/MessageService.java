package com.example.derek.gymbuddy;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Service class to receive messages from wearable device
 */
public class MessageService extends WearableListenerService {

    public static final String TAG = "MessageServiceApp";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "Message Received");
        //path that must match from the receiver
        if (messageEvent.getPath().equals("/my_path")) {
            final String message = new String(messageEvent.getData());

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }//end if
        else {
            super.onMessageReceived(messageEvent);
        }//end else
    }//end onMessageRecieve
}//end class
