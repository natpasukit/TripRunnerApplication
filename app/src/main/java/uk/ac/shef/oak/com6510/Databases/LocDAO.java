package uk.ac.shef.oak.com6510.Databases;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


/**
 * Define a interface to query the location and sensor data
 */

@Dao
public interface LocDAO {
    @Insert
    void insertAll(LocAndSensorData... locAndSensorData);

    @Insert
    void insert(LocAndSensorData locAndSensorData);

    @Delete
    void delete(LocAndSensorData locAndSensorData);

    /**
     * it selects the latest element
     * @return a live data of a table row
     */
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC LIMIT 1")
    LiveData<LocAndSensorData> retrieveOneData();

    /**
     * it selects the latest element
     * @return a table row
     */
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC LIMIT 1")
    LocAndSensorData retrieveLatestLocData();

    /**
     * retrieve the latest info by trip id
     * @return a table row
     */
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY tripId DESC LIMIT 1")
    LocAndSensorData retrieveLatestTripData();

    /**
     * get all data sort by id
     * @return a livedata of table row
     */
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC")
    LiveData<LocAndSensorData> retrieveAllData();

    /**
     * retieve the all rows with different trip names
     * @return a cusor for a list of table row
     */
    @Query("SELECT tripId as _id, tripName, max(timeStamp) as tripEnd  FROM loc_and_sensor_data GROUP BY tripId ORDER BY id DESC")
    Cursor retrieveAllTrip();

    /**
     * retrieve all points in one trip
     * @return a cusor for a list of table row
     */
    @Query("SELECT * FROM loc_and_sensor_data WHERE tripId = :tripId ORDER BY id ASC")
    Cursor retrieveAllPointsInOneTrip(int tripId);
}
