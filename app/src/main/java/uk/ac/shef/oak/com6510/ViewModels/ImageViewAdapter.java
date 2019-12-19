package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

import uk.ac.shef.oak.com6510.Databases.MapRepository;

public class ImageViewAdapter {
    private Application application;
    private Context context;
    private MapRepository mapRepository;
    private Cursor pointsCursorList;
    private CursorAdapter cursorAdapter;

    public ImageViewAdapter(Application application, Context context,int tripId){
        this.application = application;
        this.context = context;
        this.mapRepository = new MapRepository(application);

        this.pointsCursorList = this.mapRepository.getAllPointsInOneTrip(tripId);

    }
}
