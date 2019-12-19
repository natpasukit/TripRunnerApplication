package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.Databases.MapRepository;

public class ImageViewAdapter {
    private Application application;
    private Context context;
    private MapRepository mapRepository;
    private Cursor pointsCursorList;
    private ArrayList<LocAndSensorData> pointsInfo;
    private String tripName;

    public ImageViewAdapter(Application application, Context context, int tripId) {
        this.application = application;
        this.context = context;
        this.mapRepository = new MapRepository(application);

        this.pointsCursorList = this.mapRepository.getAllPointsInOneTrip(tripId);
        this.pointsInfo = new ArrayList<>();

        for (this.pointsCursorList.moveToFirst(); !this.pointsCursorList.isAfterLast(); this.pointsCursorList.moveToNext()) {
            int currId = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("id"));
            float currPressureValue = this.pointsCursorList.getFloat(this.pointsCursorList.getColumnIndex("preasureValue"));
            int currPressureAccurancy = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("preasureVauleAccurany"));
            Double currLon = this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("longitude"));
            Double currLat = this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("latitude"));

            int currTripId = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("tripId"));
            String currTripName = this.pointsCursorList.getString(this.pointsCursorList.getColumnIndex("tripName"));

            float currTemperature = this.pointsCursorList.getFloat(this.pointsCursorList.getColumnIndex("temperatureValue"));
            long currTimeStamp = this.pointsCursorList.getLong(this.pointsCursorList.getColumnIndex("timeStamp"));

            tripName = currTripName;

            LocAndSensorData l = new LocAndSensorData(currTimeStamp,currTemperature,currPressureValue,currPressureAccurancy,currLat,currLon,currTripId,currTripName);
            l.setId(currId);
            pointsInfo.add(l);
        }
    }

    public ArrayList<LatLng> getAllPointsLoc(){
        ArrayList<LatLng> p = new ArrayList<>();
        for(LocAndSensorData l : pointsInfo){
            p.add(new LatLng(l.getLatitude(),l.getLongitude()));
        }
        return p;
    }

    public LocAndSensorData getInfoById(int id){
        for(LocAndSensorData l : pointsInfo) {
            if(l.getId() == id){
                return l;
            }

        }
        return null;
    }

    public String getTripName() {
        return tripName;
    }
}
