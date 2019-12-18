package uk.ac.shef.oak.com6510.Databases;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LocAndSensorDataAndPhotoRelation {
    @Embedded public LocAndSensorData locAndSensorData;
    @Relation(
            parentColumn = "id",
            entityColumn = "tripStopId",
            entity = PhotoEntity.class
    )
    public List<PhotoEntity> photoEntities;
}