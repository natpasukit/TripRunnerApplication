package uk.ac.shef.oak.com6510.Models;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.ViewModels.MapViewModel;

public class MyMap {

    private static final int ACCESS_FINE_LOCATION = 123;
    private static Barometer barometer;
    private static Temperature temperature;
    private static ArrayList<LatLng> latLngs;
    private static int tripNumber;
    private static MapViewModel mapViewModel;
    private static String tripName;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    private boolean started;
    private Location mCurrentLocation;
    private long mLastUpdateTime;
    private PendingIntent mLocationPendingIntent;

    public MyMap(Context context, String tripName, Barometer barometer, Temperature temperature, MapViewModel mapViewModel) {
        this.context = context;
        this.barometer = barometer;
        this.mapViewModel = mapViewModel;
        this.tripName = tripName;
        this.temperature = temperature;
        this.started = false;
        this.tripNumber = this.mapViewModel.getLatestTripId() + 1;
        this.latLngs = new ArrayList<LatLng>();
    }

    /**
     * ask for access to the location permission and
     * start the a new location tracking intent service
     */
    public boolean startLocationIntentService() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }

        startAndInitLocIntentService();
        return true;
    }

    /**
     * start the location tracking intent service
     */
    private void startAndInitLocIntentService() {
        Intent intent = new Intent(context, LocationIntent.class);
        mLocationPendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Task<Void> locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationPendingIntent);
        if (locationTask != null) {
            locationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ApiException) {
                        Log.w("Error", ((ApiException) e).getStatusMessage());
                    } else {
                        Log.w("Error", e.getMessage());
                    }
                }
            });

            locationTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Service", "start location intent service successfully");
                }
            });
        }
    }

    /**
     * stop the location updates
     */
    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationPendingIntent);
    }

    /**
     * call at the activity onResume
     * init and start the location intent service
     */
    public void resume() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        startLocationIntentService();
    }

    /**
     * let location intent service to insert a row into the table
     * it will automatically get the other info needed
     *
     * @param lat latitude
     * @param lon longitude
     */
    public static void insertNewData(double lat, double lon) {
        latLngs.add(new LatLng(lat, lon));
        LocAndSensorData l = new LocAndSensorData(System.currentTimeMillis(), temperature.getLatestValue(), barometer.getLatestValue(), barometer.getLatestAccuracy(), lat, lon, tripNumber, tripName);
        mapViewModel.insertOneData(l);
    }

    /**
     * get permission of location
     *
     * @param requestCode  Integer request code
     * @param permissions
     * @param grantResults
     */
    public void permissionsResult(int requestCode,
                                  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startAndInitLocIntentService();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("Permission", "deny");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean getStarted() {
        return started;
    }

    public static ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }
}
