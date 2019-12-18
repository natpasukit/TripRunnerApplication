package uk.ac.shef.oak.com6510.Databases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Define photo database entity
 * photoId as a primary key
 * tripId refer to tripId in LocAndSensorData
 * tripStopId refer to id in LocAndSensorData
 */
@Entity(tableName = "photo_information")
public class PhotoEntity {
    // Autogenerate photo id
    @PrimaryKey(autoGenerate = true)
    private int photoId;

    private int tripId;
    private int tripStopId;
    private String photoFileDirectory;
    private String photoDate;

    /**
     * @// TODO: Use type converter to create datetime for photo
     */
    // ISO8601 strings (“YYYY-MM-DD HH:MM:SS.SSS”)
    // @TypeConverter({TimestampConverter.class})
    // public Date lastModified;
    public PhotoEntity(int tripId, int tripStopId, String photoFileDirectory, String photoDate) {
        this.tripId = tripId;
        this.tripStopId = tripStopId;
        this.photoFileDirectory = photoFileDirectory;
        this.photoDate = photoDate;
    }

    public int getTripId() {
        return tripId;
    }

    public int getTripStopId() {
        return tripStopId;
    }

    public String getPhotoDate() {
        return photoDate;
    }

    public String getPhotoFileDirectory() {
        return photoFileDirectory;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoDate(String photoDate) {
        this.photoDate = photoDate;
    }

    public void setPhotoFileDirectory(String photoFileDirectory) {
        this.photoFileDirectory = photoFileDirectory;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setTripStopId(int tripStopId) {
        this.tripStopId = tripStopId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
