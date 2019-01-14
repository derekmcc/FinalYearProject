package com.example.derek.gymbuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.derek.gymbuddy.logger.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RepCounterActivity extends BaseActivity  {

    //global scope variables
    public static final String TAG = "RepCounterActivity";

    GoogleApiClient googleClient;
    protected Handler myHandler;
    int receivedMessageNumber = 1;
    int sentMessageNumber = 1;
    TextView numRepsTxt, currentSetTxt, repsRemainingTxt;
    Button btn;
    ProgressBar progressBar;
    int progress = 0;
    private String weight;
    private int sets, reps;
    private int currentSet = 1, repsRemaining = 0, currentReps = 0;
    private ArrayList<String> alSets;
    ListView setList;
    ArrayAdapter<String> adapter;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_counter);

        //assign the values to the components
        //btn = findViewById(R.id.button);
        numRepsTxt = findViewById(R.id.txtNumReps);
        currentSetTxt = findViewById(R.id.txtNumSets);
        progressBar = findViewById(R.id.progressbarBar);
        repsRemainingTxt = findViewById(R.id.txtRepsRemaining);

        //get the data from the intent
        weight = getIntent().getStringExtra("weight");
        reps = getIntent().getIntExtra("reps", 0);
        sets = getIntent().getIntExtra("sets", 0);

        alSets = new ArrayList<String>();
        for (int i = 0; i < sets; i++){
            //if this is the firs element in the array list
            if (i == 0){
                alSets.add("Set " + (i+1) + " of " + sets + " in progress");
            }//end if
            else {
                alSets.add("Set " + (i+1) + " of " + sets + " waiting");
            }//end else
        }//end for

        // set up the ListView to use the lines from the file
        setList = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, alSets);
        setList.setAdapter(adapter);

        String strCurrentSet = "Set " + currentSet;
        repsRemaining = reps;
        currentSetTxt.setText(strCurrentSet);
        String repsRemaing = "Reps Remaining " + reps;
        repsRemainingTxt.setText(repsRemaing);
        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                messageText(stuff.getString("messageText"));
                return true;
            }
        });
        progressBar.setMax(reps);
        talkClick();//For calling activity---------------------------
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    public void messageText(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            numRepsTxt.append("\n" + newinfo);
        }
    }

    public void updateAdapter(){
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, alSets);
        setList.setAdapter(adapter);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String in = String.valueOf(intent.hasExtra("message"));

            String strRepsRemaining = "";
            String strCurrentReps = "";
            currentReps++;
            String in = intent.getStringExtra("message");
            android.util.Log.d(TAG, in);
            label:if (!flag) {
                android.util.Log.d(TAG,"Flag status " + flag);
                repsRemaining = reps - currentReps;
                progress++;
                if (currentReps == reps) {
                    Log.d(TAG, "Current Set " + currentSet);
                    Log.d(TAG, "Reps " + reps);
                    String tempSet = "Set " + currentSet + " of " + sets + " COMPLETED";
                    alSets.set(currentSet - 1, tempSet);
                    progress = 0;
                    repsRemaining = reps;
                    currentSet++;

                    String strSet = "Set " + currentSet;
                    currentSetTxt.setText(strSet);
                    progressBar.setProgress(progress);

                    strRepsRemaining = "Reps Remaining " + reps;
                    repsRemainingTxt.setText(strRepsRemaining);
                    if (currentSet == sets + 1) {
                        android.util.Log.d(TAG,"Setting flag to True");
                        flag = true;
                       // break label;
                    }
                    currentReps = 0;
                    if (!flag) {
                        alSets.set(currentSet - 1, "Set " + currentSet + " of " + sets + " in progress");
                    }
                    updateAdapter();
                }
                if (currentSet > sets){
                    alSets.set(currentSet-2, "Set " + (currentSet-1) + " of " + sets + " COMPLETED");
                    progressBar.setProgress(progress);
                    String strSet = "Set " + (currentSet-1);
                    currentSetTxt.setText(strSet);
                    strRepsRemaining = "All Sets Complete";
                    repsRemainingTxt.setText(strRepsRemaining);
                    strCurrentReps = "Rep " + currentReps;
                    numRepsTxt.setText(strCurrentReps);
                    progressBar.setProgress(100);
                } else {
                    alSets.set(currentSet - 1, "Set " + currentSet + " of " + sets + " in progress");
                    progressBar.setProgress(progress);
                    strRepsRemaining = "Reps Remaining " + repsRemaining;
                    repsRemainingTxt.setText(strRepsRemaining);
                    strCurrentReps = "Rep " + currentReps;
                    numRepsTxt.setText(strCurrentReps);
                }
                //progressBar.setProgress(progress);
                android.util.Log.d(TAG,"Flag status " + flag);
            }
            else if (flag && currentSet != sets){
                String strSet = "Set " + (currentSet-1);
                strRepsRemaining = "All Sets Complete";
                repsRemainingTxt.setText(strRepsRemaining);
                numRepsTxt.setText(strCurrentReps);

                android.util.Log.d(TAG,"Flag status " + flag);
                android.util.Log.d(TAG,"Flag is True");
                strCurrentReps = "Rep " + currentReps;
                numRepsTxt.setText(strCurrentReps);
                alSets.set(currentSet-2, "Set " + (currentSet-1) + " of " + sets + " COMPLETED");
                updateAdapter();
            }
            String message = "Rep " + receivedMessageNumber++;
        }//end onReceive
    }//end Receiver

    public void talkClick() {
        Log.d("RepCounter", "On receive called");
        String message = "Sending message.... ";
        //numRepsTxt.setText(message);
        new NewThread("/my_path", message).start();
    }


    public void sendmessage(String messageText) {
        Bundle bundle = new Bundle();
        bundle.putString("messageText", messageText);
        Message msg = myHandler.obtainMessage();
        msg.setData(bundle);
        myHandler.sendMessage(msg);
    }


    class NewThread extends Thread {
        String path;
        String message;

        NewThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {

            Task<List<Node>> wearableList =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {

                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(RepCounterActivity.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                        sendmessage("Establishing Connection...");

                    } catch (ExecutionException exception) {
                        //TO DO: Handle the exception//
                    } catch (InterruptedException exception) {

                    }

                }

            } catch (ExecutionException exception) {

                //TO DO: Handle the exception//

            } catch (InterruptedException exception) {

                //TO DO: Handle the exception//
            }

        }
    }
}
