package uk.ac.shef.oak.com6510.Databases;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "loc_and_sensor_data")
public class LocAndSensorData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id = 0;

    private float preasure_value;
    private int preasure_vaule_accurany;
    private float temperature_value;

    private double longitude;
    private double latitude;
    private int tripId;
    private String tripName;
    private String timeStamp;

    public LocAndSensorData(String timeStamp,float temperature_value, float preasure_value, int preasure_vaule_accurany, double latitude, double longitude, int tripId, String tripName) {
        this.preasure_value = preasure_value;
        this.preasure_vaule_accurany = preasure_vaule_accurany;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tripId = tripId;
        this.tripName = tripName;
        this.temperature_value = temperature_value;
        this.timeStamp = timeStamp;
    }

    public float getPreasure_value() {
        return preasure_value;
    }

    public int getId() {
        return id;
    }

    public int getPreasure_vaule_accurany() {
        return preasure_vaule_accurany;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getTemperature_value() {
        return temperature_value;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public String getTimeStamp() { return timeStamp; }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPreasure_value(float preasure_value) {
        this.preasure_value = preasure_value;
    }

    public void setPreasure_vaule_accurany(int preasure_vaule_accurany) { this.preasure_vaule_accurany = preasure_vaule_accurany; }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setTemperature_value(float temperature_value) { this.temperature_value = temperature_value; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "latitude: " + latitude + ",longitude: " + longitude + ",barometer: " + preasure_value + ",tripId: " + tripId + ",trip name: " + tripName;
    }
}
