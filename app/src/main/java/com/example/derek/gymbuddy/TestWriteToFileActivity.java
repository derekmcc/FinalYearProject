//package com.example.derek.gymbuddy;
//
//import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//public class TestWriteToFileActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_write_to_file);
//
//        String text = readFromFile(this);
//        TextView tv = findViewById(R.id.textView);
//        tv.setText(text);
//    }
//
//    private String readFromFile(Context context) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("routine.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return ret;
//    }
//}
