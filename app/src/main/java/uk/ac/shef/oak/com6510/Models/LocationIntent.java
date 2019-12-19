package uk.ac.shef.oak.com6510.Models;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

public class LocationIntent extends IntentService {
    public LocationIntent(String name){
        super(name);
    }

    public LocationIntent(){
        super("Location Intent");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        if (LocationResult.hasResult(intent)){
            LocationResult locationResult = LocationResult.extractResult(intent);
            if(locationResult != null){
                for(Location location : locationResult.getLocations()){
                    if(location == null) continue;
                    Log.i("New Intent Location","Current location: "+location);
                    MyMap.insertNewData(location.getLatitude(),location.getLongitude());
                }
            }
        }
    }
}