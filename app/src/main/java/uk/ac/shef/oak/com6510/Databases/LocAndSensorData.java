package uk.ac.shef.oak.com6510.Databases;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Define a table to store the location and sensor data
 *
 * id is the primary key
 *
 * pressureValue and pressureValueAccurancy to store the barometer data
 * temperatureValue to store the temperature data
 *
 * latitude and longitude to store the location info
 *
 * tripId and tripName to store the trip info
 *
 * timestamp to store the time of create the data
 */

@Entity(tableName = "loc_and_sensor_data")
public class LocAndSensorData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id = 0;

    private float preasureValue;
    private int preasureVauleAccurancy;
    private float temperatureValue;

    private double longitude;
    private double latitude;
    private int tripId;
    private String tripName;
    private long timeStamp;

    public LocAndSensorData(long timeStamp,float temperatureValue, float preasureValue, int preasureVauleAccurancy, double latitude, double longitude, int tripId, String tripName) {
        this.preasureValue = preasureValue;
        this.preasureVauleAccurancy = preasureVauleAccurancy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tripId = tripId;
        this.tripName = tripName;
        this.temperatureValue = temperatureValue;
        this.timeStamp = timeStamp;
    }

    public float getPreasureValue() {
        return preasureValue;
    }

    public int getId() {
        return id;
    }

    public int getPreasureVauleAccurancy() {
        return preasureVauleAccurancy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getTemperatureValue() {
        return temperatureValue;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public long getTimeStamp() { return timeStamp; }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPreasureValue(float preasure_value) {
        this.preasureValue = preasure_value;
    }

    public void setPreasureVauleAccurancy(int preasureVauleAccurancy) { this.preasureVauleAccurancy = preasureVauleAccurancy; }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setTemperatureValue(float temperatureValue) { this.temperatureValue = temperatureValue; }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "latitude: " + latitude + ",longitude: " + longitude + ",barometer: " + preasureValue + ",tripId: " + tripId + ",trip name: " + tripName;
    }
}
