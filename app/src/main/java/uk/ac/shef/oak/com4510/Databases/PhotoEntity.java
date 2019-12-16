package uk.ac.shef.oak.com4510.Databases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

import uk.ac.shef.oak.com4510.Utils.TimestampConverter;

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
    public int photoId;

    public int tripId;
    public int tripStopId;
    public String photoFileDirectory;
    public String photoDate;

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
}
