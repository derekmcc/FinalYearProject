package com.example.derek.gymbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "MapsActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static int PROXIMITY_RADIUS = 4000;
    private static final String GYM = "gym";
    private GoogleMap mMap;
    double latitude;
    double longitude;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Gyms Nearby");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }//end if

        //check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d(TAG , "Finishing test case since Google Play Services are not available");
            finish();
        }//end if
        else {
            Log.d(TAG ,"Google Play Services available.");
        }//end else

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }//end onCreate method

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }//end if
            return false;
        }//end if
        return true;
    }//end CheckGooglePlayServices method

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }//end if
        }//end if
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }//end else
    }//end onMapReady method

    public void gymsNearBy(){
        String url = getUrl(latitude, longitude);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d(TAG, url);
        GetNearbyGymsData getNearbyPlacesData = new GetNearbyGymsData();
        getNearbyPlacesData.execute(DataTransfer);
    }//end gymsNearBy method

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }//end buildGoogleApiClient method

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }//end if
    }//end onConnected method

    private String getUrl(double latitude, double longitude) {
        Log.d("Map","GetUrl Called");
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=").append(GYM);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyChy4DFRBwc6-3rtNTqA0-LdkhnA5nBLL4");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }//end getUrl

    @Override
    public void onConnectionSuspended(int i) {

    }//end onConnectionSuspended( method

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged method");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }//end if

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.d(TAG, String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d(TAG, "Removing Location Updates");
        }//end if
        Log.d(TAG, "Exit");
        gymsNearBy();
    }//end onLocationChanged

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }//end onConnectionFailed method



    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }//end if
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }//end else
            return false;
        }//end else
        else {
            return true;
        }//end else
    }//end checkLocationPermission method

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }//end if
                        mMap.setMyLocationEnabled(true);
                    }//end if

                }//end else
                else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }//end if
            }//end case

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }//end switch
    }//end onRequestPermissionsResult method


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }//end onCreateOptionsMenu method
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
        finish();
    }//end onBackPressed

    /**
     * Method to handle button clicks on bottom navigation
     * @param view Item Clicked
     */
    public void buttonListener(View view) {
        //check which button has been clicked
        if(view == findViewById(R.id.planner)){
            //log message
            Log.d(TAG,"Going to Planner Activity");
            finish();
            //go to the planner activity
            Intent i = new Intent(this, PlannerActivity.class);
            startActivity(i);
        }//end if planner
        else if (view.getId() == R.id.repCount) {
            finish();
            //go to the planner activity
            Intent i = new Intent(this, RepCounterListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.history) {
            finish();
            //go to the weight history activity
            Intent i = new Intent(this, WeightHistoryListActivity.class);
            startActivity(i);
        }//end else if
        else if (view.getId() == R.id.lookup) {
            //go to the 3D Avatar activity
            Intent intent = new Intent(this, UnityPlayerActivity.class);
            startActivity(intent);
        }//end else if
        else if (view.getId() == R.id.mainMenu) {
            finish();
            //go to the leaderboard activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }//end else if
    }//end buttonListener method
}//end class
