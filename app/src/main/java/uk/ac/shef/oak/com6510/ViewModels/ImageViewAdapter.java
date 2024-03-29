package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.GalleryRepository;
import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.Databases.MapRepository;
import uk.ac.shef.oak.com6510.Views.GalleryActivity;

/**
 * a class helps imageActivity to process data
 * get the trip name route location and photo path
 */
public class ImageViewAdapter {
    private Application application;
    private Context context;
    private MapRepository mapRepository;
    private Cursor pointsCursorList;
    private Cursor pictureCursorList;
    private ArrayList<LocAndSensorData> pointsInfo;
    private String tripName;
    private GalleryRepository galleryRepository;
    private ArrayList<LatLng> photoPoints;

    /**
     * init the adpter take the trip id to process the specific trip
     * @param application get the current application
     * @param context get current context
     * @param tripId get the target trip id
     */
    public ImageViewAdapter(Application application, Context context, int tripId) {
        this.application = application;
        this.context = context;
        this.mapRepository = new MapRepository(application);
        this.galleryRepository = new GalleryRepository(application);

        this.pointsCursorList = this.mapRepository.getAllPointsInOneTrip(tripId);
        this.pictureCursorList = this.galleryRepository.getAllPhotoInOneTripInformation(tripId);
        this.pointsInfo = new ArrayList<>();
        this.photoPoints = new ArrayList<>();

        //loop the cursor to restore the table row
        for (this.pointsCursorList.moveToFirst(); !this.pointsCursorList.isAfterLast(); this.pointsCursorList.moveToNext()) {
            int currId = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("id"));
            float currPressureValue = this.pointsCursorList.getFloat(this.pointsCursorList.getColumnIndex("preasureValue"));
            int currPressureAccurancy = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("preasureVauleAccurancy"));
            Double currLon = this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("longitude"));
            Double currLat = this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("latitude"));

            int currTripId = this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("tripId"));
            String currTripName = this.pointsCursorList.getString(this.pointsCursorList.getColumnIndex("tripName"));

            float currTemperature = this.pointsCursorList.getFloat(this.pointsCursorList.getColumnIndex("temperatureValue"));
            long currTimeStamp = this.pointsCursorList.getLong(this.pointsCursorList.getColumnIndex("timeStamp"));

            tripName = currTripName;

            LocAndSensorData l = new LocAndSensorData(currTimeStamp, currTemperature, currPressureValue, currPressureAccurancy, currLat, currLon, currTripId, currTripName);
            l.setId(currId);
            pointsInfo.add(l);
        }

        for(this.pictureCursorList.moveToFirst(); !this.pictureCursorList.isAfterLast(); this.pictureCursorList.moveToNext()){
            for(LocAndSensorData l : pointsInfo) {
                int currStopId = this.pictureCursorList.getInt(this.pictureCursorList.getColumnIndex("tripStopId"));
                if (currStopId == l.getId()) {
                    photoPoints.add(new LatLng(l.getLatitude(),l.getLongitude()));
                }
            }
        }

    }

    /**
     * get all the location data for LocAndSensorData
     * @return an ArrayList of LatLng of all the points
     */
    public ArrayList<LatLng> getAllPointsLoc(){
        ArrayList<LatLng> p = new ArrayList<>();
        for (LocAndSensorData l : pointsInfo) {
            p.add(new LatLng(l.getLatitude(), l.getLongitude()));
        }
        return p;
    }

    /**
     * use id to get a specific row of data
     * @param id to identify the row
     * @return a object of LocAndSensorData
     */
    public LocAndSensorData getInfoById(int id) {
        for (LocAndSensorData l : pointsInfo) {
            if (l.getId() == id) {
                return l;
            }

        }
        return null;
    }

    public String getTripName() {
        return tripName;
    }

    public ArrayList<LatLng> getPhotoPoints() {
        return photoPoints;
    }
}
