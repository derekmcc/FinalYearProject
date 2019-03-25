package com.example.derek.gymbuddy;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.derek.gymbuddy.events.DatabaseHelper;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeightHistoryActivity extends AppCompatActivity {

    private static final String TAG = "WeightHistoryActivity";

    DatabaseHelper databaseHelper;
    Cursor c, data;
    ArrayList <String> weightItem = new ArrayList<String>();
    ArrayList <String> dateItem = new ArrayList<String>();
    Date firstDate;
    GraphView graphView;
    String routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_history);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        graphView = findViewById(R.id.graph);
        routine = getIntent().getStringExtra("routine");
        setTitle(routine + " Weight History");
        databaseHelper = new DatabaseHelper(this, routine);
        try {
            populateGraph();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private  void readInData() {
        Cursor data = databaseHelper.getData();
        while (data.moveToNext()) {
            weightItem.add(data.getString(1));
//            dateItem.add(data.getString(2));
            Log.d(TAG,"Weight: "+weightItem);
        }
        data.close();
    }

    private void populateGraph() throws ParseException {
        Log.d(TAG, "Populating Graph");
        readInData();

        int size = weightItem.size();
        DataPoint[] dataPoints = null;
        dataPoints = new DataPoint[size];
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");

        for(int i = 0; i < size; i++) {
          //  Date date = formatter1.parse(dateItem.get(i).toString());

            if (i == 0){
                //firstDate = new Date(date.getTime());
            }
            int weight = Integer.parseInt(weightItem.get(i).replace(" Kg",""));

            dataPoints[i] = new DataPoint(i, weight);

        }//end for
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 10),
//                new DataPoint(1, 50),
//                new DataPoint(2, 130)
//        });
        graphView.addSeries(series);

        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        //graphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();

        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(200);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(size);
        graphView.getViewport().setYAxisBoundsManual(true);

       // graphView.getViewport().setXAxisBoundsManual(true);
        //graphView.getGridLabelRenderer().setHumanRounding(false);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " Kg";
                }
            }
        });


        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.getViewport().setScrollableY(true); // enables vertical scrolling
        graphView.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphView.getViewport().setScalableY(true); // enables vertical zooming and scrolling

    }

}
