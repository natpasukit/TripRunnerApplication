package uk.ac.shef.oak.com4510.Databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PhotoDAO {
    @Insert
    void insertAll(PhotoEntity... photoEntities);

    @Insert
    void insert(PhotoEntity photoEntity);

    @Delete
    void delete(PhotoEntity photoEntity);

//    @Query("SELECT * FROM photo_information WHERE tripStopId = :markerId")
//    public PhotoEntity selectPhotoWithMarkerId(int markerId);
//
//    @Query("SELECT * FROM photo_information WHERE tripId = :tripId")
//    public PhotoEntity[] selectAllPhotoWithTripId(int tripId);
//
//    @Query("SELECT * FROM photo_information WHERE tripId = :tripId LIMIT :length")
//    public PhotoEntity[] selectAllPhotoWithTripId(int tripId,int length);
}