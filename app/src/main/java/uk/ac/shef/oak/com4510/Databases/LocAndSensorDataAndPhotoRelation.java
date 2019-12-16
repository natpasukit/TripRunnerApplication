package uk.ac.shef.oak.com4510.Databases;

import androidx.room.Embedded;
import androidx.room.Relation;

public class LocAndSensorDataAndPhotoRelation {
    @Embedded public LocAndSensorData locAndSensorData;
    @Relation(
            parentColumn = "id",
            entityColumn = "tripStopId"
    )
    public PhotoEntity photoEntity;
}