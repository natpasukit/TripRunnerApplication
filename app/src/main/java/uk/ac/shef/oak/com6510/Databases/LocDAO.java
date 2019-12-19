package uk.ac.shef.oak.com6510.Databases;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LocDAO {
    @Insert
    void insertAll(LocAndSensorData... locAndSensorData);

    @Insert
    void insert(LocAndSensorData locAndSensorData);

    @Delete
    void delete(LocAndSensorData locAndSensorData);

    // it selects a random element
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC LIMIT 1")
    LiveData<LocAndSensorData> retrieveOneData();

    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC LIMIT 1")
    LocAndSensorData retrieveLatestLocData();

    @Query("SELECT * FROM loc_and_sensor_data ORDER BY tripId DESC LIMIT 1")
    LocAndSensorData retrieveLatestTripData();

    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC")
    LiveData<LocAndSensorData> retrieveAllData();

    @Delete
    void deleteAll(LocAndSensorData... locAndSensorData);

    @Query("SELECT COUNT(*) FROM loc_and_sensor_data")
    int howManyElements();

    @Query("SELECT tripId as _id, tripName, max(timeStamp) as tripEnd  FROM loc_and_sensor_data GROUP BY tripId ORDER BY id DESC")
    Cursor retrieveAllTrip();
}
