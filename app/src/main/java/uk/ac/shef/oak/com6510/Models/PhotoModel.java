package uk.ac.shef.oak.com6510.Models;

import android.app.Application;

import uk.ac.shef.oak.com6510.Databases.PhotoEntity;
import uk.ac.shef.oak.com6510.Databases.PhotoRepository;

/**
 * PhotoModel to create photo object.
 */
public class PhotoModel {
    private final PhotoRepository photoRepository;
    private String photoPath;
    private String photoDate;
    private int tripId;
    private int tripStopId;
    private PhotoEntity photoEntity;

    /**
     * Create photoModel object.
     *
     * @param application Application activity to allow to insert this into repository.
     * @param photoPath   String, Absolute path to image that this application can access to.
     * @param photoDate   String, Date of this picture in string format
     * @param tripId      Integer, Id of trip that this photo trip belong to.
     * @param tripStopId  Integer, Id of stop in each trip that this photo belong to.
     */
    public PhotoModel(Application application, String photoPath, String photoDate, int tripId, int tripStopId) {
        photoRepository = new PhotoRepository(application);

        this.photoPath = photoPath;
        this.photoDate = photoDate;
        this.tripId = tripId;
        this.tripStopId = tripStopId;
        this.photoEntity = new PhotoEntity(this.tripId, this.tripStopId, this.photoPath, this.photoDate);

    }

    /**
     * Insert current photo into PhotoEntity to insert into database using photoRepository.
     */
    public void insertPhotoToDb() {
        photoRepository.insertOnePhotoData(photoEntity);
    }
}
