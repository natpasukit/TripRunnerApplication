package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.Databases.MapRepository;

/**
 * the live model for map to get updated on the latest location data
 */
public class MapViewModel extends AndroidViewModel {
    private final MapRepository mapRepository;

    public LiveData<LocAndSensorData> locAndSensorDataLiveData;
    public int stopId;

    /**
     * init the view model
     * @param application
     */
    public MapViewModel(Application application){
        super(application);
        mapRepository = new MapRepository(application);
        locAndSensorDataLiveData = mapRepository.getLatestData();
    }

    public int getLatestTripId() {
        return mapRepository.getLatestTripId();
    }

    public int getStopId() {
        return mapRepository.getStopId();
    }

    public LatLng getLatestLoc() {
        return mapRepository.getLatestLoc();
    }

    /**
     * get the live data of location
     * @return a livedata of LocAndSensorData
     */
    public LiveData<LocAndSensorData> getLocAndSensorDataLiveData(){
        if (locAndSensorDataLiveData == null) {
            locAndSensorDataLiveData = new MutableLiveData<LocAndSensorData>();
        }
        return locAndSensorDataLiveData;
    }

    public void insertOneData(LocAndSensorData l) {
        mapRepository.insertOneData(l);
    }
}
