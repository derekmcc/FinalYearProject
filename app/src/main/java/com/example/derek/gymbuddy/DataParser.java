package com.example.derek.gymbuddy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataParser {

    private static final String TAG = "DataParser";

    public List<HashMap<String, String>> parse(String jsonData) {

        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d(TAG, "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d(TAG, "parse error");
            e.printStackTrace();
        }//end catch
        return getPlaces(jsonArray);
    }//end List method

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d(TAG, "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d(TAG, "Adding places");

            } catch (JSONException e) {
                Log.d(TAG, "Error in Adding places");
                e.printStackTrace();
            }//end catch
        }//end for
        return placesList;
    }//end List method

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude;
        String longitude;
        String reference;

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            Log.d(TAG, "Putting Places");
        } catch (JSONException e) {
            Log.d(TAG, "Error");
            e.printStackTrace();
        }//end catch
        return googlePlaceMap;
    }//end HashMap method
}//end class
