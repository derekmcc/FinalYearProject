package com.example.derek.gymbuddy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to download local gyms data
 */
public class DownloadUrl {

    private static final String TAG = "DownloadUrl";

    /**
     * Method to read in the chosen URL
     * @param strUrl The google places data url of the gyms
     * @return The gym data
     * @throws IOException If unsuccessful
     */
    public String readUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            //create an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            //connect to url
            urlConnection.connect();

            //read data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }//end while

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }//end finally
        return data;
    }//end readUrl method
}//end class
