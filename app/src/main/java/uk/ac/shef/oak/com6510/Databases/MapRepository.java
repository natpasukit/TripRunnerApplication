package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Define a repository to get location and sensor data from room database
 */

public class MapRepository extends ViewModel {
    private final LocDAO myLocDao;

    public MapRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myLocDao = db.myLocDao();
    }

    /**
     * use Asynctask to insert one row into the table
     *
     * @param locAndSensorData a new row in the table
     */
    public void insertOneData(@NonNull LocAndSensorData locAndSensorData) {
        new insertLocAsyncTask(myLocDao).execute(locAndSensorData);
    }

    /**
     * get lastest id of trip from loc database in descending order
     *
     * @return Integer trip id, return -1 if no data
     * @throws Exception on error
     */
    public int getLatestTripId() {
        try {
            LocAndSensorData locAndSensorData = new getLatestTripAsyncTask(myLocDao).execute().get();
            if (locAndSensorData != null)
                return locAndSensorData.getTripId();
            else
                return -1;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * get the latest the id of the table row
     *
     * @return Integer id, return 0 if there is no data
     * @throws Exception on error
     */
    public int getStopId() {
        try {
            LocAndSensorData locAndSensorData = new getLatestLocAsyncTask(myLocDao).execute().get();
            if (locAndSensorData != null)
                return locAndSensorData.getId();
            else
                return 0;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * get the latest location
     *
     * @return LatLng the latest location in the database
     * @throws Exception on error
     */
    public LatLng getLatestLoc() {
        try {
            LocAndSensorData locAndSensorData = new getLatestLocAsyncTask(myLocDao).execute().get();
            if (locAndSensorData != null)
                return new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude());
            else
                return new LatLng(0, 0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new LatLng(0, 0);
    }

    /**
     * get all the rows grouped by the trip id
     *
     * @return Cursor for a list of table row
     * @throws Exception on error
     */
    public Cursor getAllTripName(int order) {
        try {
            Cursor mCursor = new getAllTripNameAsyncTask(myLocDao).execute(order).get();
            if (mCursor != null) {
                return mCursor;
            } else
                return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get all the rows with the tripId
     *
     * @param tripId the flag to identify a trip
     * @return a Cursor for a list of desired row
     * @throws Exception on error
     */
    public Cursor getAllPointsInOneTrip(int tripId) {
        try {
            Cursor mCursor = new getAllPointsInOneTripAsyncTask(myLocDao).execute(tripId).get();
            if (mCursor != null) {
                return mCursor;
            } else
                return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get the latest location data in the table
     *
     * @return a Cursor for latest row
     * @throws Exception on error
     */
    public LiveData<LocAndSensorData> getLatestData() {
        return myLocDao.retrieveOneData();
    }

    /**
     * the internal class for Async task to insert a row
     */
    private static class insertLocAsyncTask extends AsyncTask<LocAndSensorData, Void, Void> {
        private LocDAO mAsyncTaskDao;
        private LiveData<LocAndSensorData> numberData;

        insertLocAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         *
         * @param params a list of table that need to insert into the data
         * @return null
         */
        @Override
        protected Void doInBackground(final LocAndSensorData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyMapRepository", params[0].toString());
            return null;
        }
    }

    /**
     * the internal class for Async task to get the latest trip id
     */
    private static class getLatestTripAsyncTask extends AsyncTask<Void, Void, LocAndSensorData> {
        private LocDAO mAsyncTaskDao;

        getLatestTripAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         *
         * @param URL no use
         * @return a object of LocAndSensorData
         */
        @Override
        protected LocAndSensorData doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve latest trip");
            return mAsyncTaskDao.retrieveLatestTripData();
        }
    }

    /**
     * the internal class for Async task to get the latest location
     */
    private static class getLatestLocAsyncTask extends AsyncTask<Void, Void, LocAndSensorData> {
        private LocDAO mAsyncTaskDao;

        getLatestLocAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         *
         * @param URL
         * @return a object of LocAndSensorData
         */
        @Override
        protected LocAndSensorData doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve latest location");
            return mAsyncTaskDao.retrieveLatestLocData();
        }
    }

    /**
     * the internal class for Async task to get all the rows grouped by the tripId
     */
    private static class getAllTripNameAsyncTask extends AsyncTask<Integer, Void, Cursor> {
        private LocDAO mAsyncTaskDao;

        getAllTripNameAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         * @param parm use to decide the order, 1 is for ascending ,-1 is for descending
         * @return A Cursor of a list of table rows
         */
        @Override
        protected Cursor doInBackground(Integer... parm) {
            Log.i("MyMapRepository", "Retieve All trip name");
            if(parm[0] == 1)
                return mAsyncTaskDao.retrieveAllTripASC();
            else
                return mAsyncTaskDao.retrieveAllTripDESC();
        }
    }

    /**
     * the internal class for Async task to get all the location points with the same tripId
     */
    private static class getAllPointsInOneTripAsyncTask extends AsyncTask<Integer, Void, Cursor> {
        private LocDAO mAsyncTaskDao;

        getAllPointsInOneTripAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         *
         * @param tripId
         * @return A Cursor of a list of table rows
         */
        @Override
        protected Cursor doInBackground(Integer... tripId) {
            Log.i("MyMapRepository", "Retieve All points in one trip");
            return mAsyncTaskDao.retrieveAllPointsInOneTrip(tripId[0]);
        }
    }
}
