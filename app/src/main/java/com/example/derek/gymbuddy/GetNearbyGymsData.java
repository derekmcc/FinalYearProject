package com.example.derek.gymbuddy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Class to get the data of gyms nearby
 */
public class GetNearbyGymsData extends AsyncTask<Object, String, String> {

    private static final String TAG = "GetNearbyGymsData";
    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;

    /**
     * Run on a background thread
     * @param params Map object parameters
     * @return Google places data
     */
    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d(TAG, "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d(TAG, "doInBackground Exit");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }//end catch
        return googlePlacesData;
    }//end doInBackground

    /**
     * Run on the UI thread
     * @param result Gym data
     */
    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute Entered");
        List<HashMap<String, String>> nearByGymsList = null;
        DataParser dataParser = new DataParser();
        nearByGymsList = dataParser.parse(result);
        Log.d(TAG, "NearByGymList: " + nearByGymsList);
        ShowNearbyGyms(nearByGymsList);
        Log.d(TAG, "onPostExecute Exit");
    }//end onPostExecute

    /**
     * Show the nearby gyms by a marker on the map
     * @param nearbyPlacesList The gyms nearby
     */
    private void ShowNearbyGyms(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d(TAG, "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lng")));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }//end for
    }//end nearByGyms
}//end class
