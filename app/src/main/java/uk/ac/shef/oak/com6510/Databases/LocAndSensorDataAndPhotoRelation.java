package uk.ac.shef.oak.com6510.Databases;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Define the relations between photo table and Loc table
 */

public class LocAndSensorDataAndPhotoRelation {
    @Embedded public LocAndSensorData locAndSensorData;
    @Relation(
            parentColumn = "id",
            entityColumn = "tripStopId",
            entity = PhotoEntity.class
    )
    public List<PhotoEntity> photoEntities;
}