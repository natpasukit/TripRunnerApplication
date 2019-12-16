package uk.ac.shef.oak.com4510.Models;

import android.app.Application;

import uk.ac.shef.oak.com4510.Databases.MyRoomDatabase;
import uk.ac.shef.oak.com4510.Databases.PhotoDAO;
import uk.ac.shef.oak.com4510.Databases.PhotoEntity;

public class PhotoModel {
    private final PhotoDAO photoDAO;
    private String photoPath;
    private String photoDate;
    int tripId;
    int tripStopId;
    private PhotoEntity photoEntity;

    public PhotoModel(Application application, String photoPath, String photoDate) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        photoDAO = db.photoDAO();
        this.photoPath = photoPath;
        this.photoDate = photoDate;
        this.tripId = 0;
        this.tripStopId = 0;
        this.photoEntity = new PhotoEntity(this.tripId, this.tripStopId, this.photoPath, this.photoDate);

    }

    /**
     * @// TODO: Change this to async task ?
     */
    public void insertPhotoToDb() {
        photoDAO.insertOne(photoEntity);
    }
}
