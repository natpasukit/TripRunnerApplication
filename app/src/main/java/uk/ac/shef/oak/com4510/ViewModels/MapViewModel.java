package uk.ac.shef.oak.com4510.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import uk.ac.shef.oak.com4510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com4510.Databases.MapRepository;
import uk.ac.shef.oak.com4510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com4510.Databases.MapRepository;

public class MapViewModel extends AndroidViewModel {
    private final MapRepository mapRepository;

    public LiveData<LocAndSensorData> locAndSensorDataLiveData;

    public MapViewModel(Application application){
        super(application);
        mapRepository = new MapRepository(application);
        locAndSensorDataLiveData = mapRepository.getLatestData();
    }

    public LiveData<LocAndSensorData> getLocAndSensorDataLiveData(){
        if (locAndSensorDataLiveData == null) {
            locAndSensorDataLiveData = new MutableLiveData<LocAndSensorData>();
        }
        return locAndSensorDataLiveData;
    }

    public void insertOneData(LocAndSensorData l) {mapRepository.insertOneData(l);}
}
