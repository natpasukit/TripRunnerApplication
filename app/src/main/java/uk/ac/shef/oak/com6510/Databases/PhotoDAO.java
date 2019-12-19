package uk.ac.shef.oak.com6510.Databases;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


/**
 * Define a interface to get the photo data
 */
@Dao
public interface PhotoDAO {

    /**
     * instert multi rows into the photo table
     *
     * @param photoEntities multi lines of table row
     */
    @Insert
    void insertAll(PhotoEntity... photoEntities);

    /**
     * instert a new row into the photo table
     *
     * @param photoEntity a new line of the data
     */
    @Insert
    void insert(PhotoEntity photoEntity);

    /**
     * Select all the photo taken on that trip
     *
     * @param tripId Integer to define the trip
     * @return a Cursor of a list of table rows
     */
    @Query("SELECT * FROM photo_information WHERE tripId = :tripId")
    Cursor selectAllPhotoWithTripId(int tripId);

    /**
     * get the latest photo info ordered by the photoId
     *
     * @return a object of PhotoEntity
     */
    @Query("SELECT * FROM photo_information ORDER BY photoId DESC LIMIT 1")
    PhotoEntity getLatestPhotoInfo();

    /**
     * get all the data in the table in ascending order
     *
     * @return a Cursor of table rows of all data
     */
    @Query("SELECT * , photoId as _id FROM photo_information ORDER BY photoId ASC")
    Cursor retrieveAllPhotoInfoASC();

    /**
     * get all the data in the table in descending order
     *
     * @return a Cursor of table rows of all data
     */
    @Query("SELECT * , photoId as _id FROM photo_information ORDER BY photoId DESC")
    Cursor retrieveAllPhotoInfoDESC();
}