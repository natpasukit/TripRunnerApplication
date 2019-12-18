package uk.ac.shef.oak.com6510.Models;

import android.app.Application;

import uk.ac.shef.oak.com6510.Databases.PhotoEntity;
import uk.ac.shef.oak.com6510.Databases.PhotoRepository;

public class PhotoModel {
    private final PhotoRepository photoRepository;
    private String photoPath;
    private String photoDate;
    int tripId;
    int tripStopId;
    private PhotoEntity photoEntity;

    public PhotoModel(Application application, String photoPath, String photoDate, int tripId, int tripStopId) {
        photoRepository = new PhotoRepository(application);

        this.photoPath = photoPath;
        this.photoDate = photoDate;
        this.tripId = tripId;
        this.tripStopId = tripStopId;
        this.photoEntity = new PhotoEntity(this.tripId, this.tripStopId, this.photoPath, this.photoDate);

    }

    public void insertPhotoToDb() {
        photoRepository.insertOnePhotoData(photoEntity);
    }
}
