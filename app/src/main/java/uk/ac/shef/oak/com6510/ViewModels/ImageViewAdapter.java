package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import uk.ac.shef.oak.com6510.Databases.MapRepository;

public class ImageViewAdapter {
    private Application application;
    private Context context;
    private MapRepository mapRepository;
    private Cursor pointsCursorList;

    public ImageViewAdapter(Application application, Context context, int tripId) {
        this.application = application;
        this.context = context;
        this.mapRepository = new MapRepository(application);

        this.pointsCursorList = this.mapRepository.getAllPointsInOneTrip(tripId);

        for (this.pointsCursorList.moveToFirst(); !this.pointsCursorList.isAfterLast(); this.pointsCursorList.moveToNext()) {
            System.out.println(this.pointsCursorList.getInt(this.pointsCursorList.getColumnIndex("tripId")));
            System.out.println(this.pointsCursorList.getString(this.pointsCursorList.getColumnIndex("tripName")));

            System.out.println(this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("longitude")));
            System.out.println(this.pointsCursorList.getDouble(this.pointsCursorList.getColumnIndex("latitude")));
        }
    }
}
