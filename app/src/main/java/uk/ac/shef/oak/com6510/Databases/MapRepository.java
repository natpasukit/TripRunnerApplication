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

public class MapRepository extends ViewModel {
    private final LocDAO myLocDao;

    public MapRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myLocDao = db.myLocDao();
    }

    public void insertOneData(@NonNull LocAndSensorData locAndSensorData) {
        new insertLocAsyncTask(myLocDao).execute(locAndSensorData);
    }

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

    public Cursor getAllTripName(){
        try {
            Cursor mCursor = new getAllTripNameAsyncTask(myLocDao).execute().get();
            if (mCursor != null) {
                return mCursor;
            }else
                return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getAllPointsInOneTrip(int tripId){
        try {
            Cursor mCursor = new getAllPointsInOneTripAsyncTask(myLocDao).execute(tripId).get();
            if (mCursor != null) {
                return mCursor;
            }else
                return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LiveData<LocAndSensorData> getLatestData() {
        return myLocDao.retrieveOneData();
    }

    private static class insertLocAsyncTask extends AsyncTask<LocAndSensorData, Void, Void> {
        private LocDAO mAsyncTaskDao;
        private LiveData<LocAndSensorData> numberData;

        insertLocAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final LocAndSensorData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyMapRepository", params[0].toString());
            return null;
        }
    }

    private static class getLatestTripAsyncTask extends AsyncTask<Void, Void, LocAndSensorData> {
        private LocDAO mAsyncTaskDao;

        getLatestTripAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LocAndSensorData doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve latest trip");
            return mAsyncTaskDao.retrieveLatestLocData();
        }
    }

    private static class getLatestLocAsyncTask extends AsyncTask<Void, Void, LocAndSensorData> {
        private LocDAO mAsyncTaskDao;

        getLatestLocAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LocAndSensorData doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve latest location");
            return mAsyncTaskDao.retrieveLatestLocData();
        }
    }

    private static class getAllTripNameAsyncTask extends AsyncTask<Void, Void, Cursor> {
        private LocDAO mAsyncTaskDao;

        getAllTripNameAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Cursor doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve All trip name");
            return mAsyncTaskDao.retrieveAllTrip();
        }
    }

    private static class getAllPointsInOneTripAsyncTask extends AsyncTask<Integer, Void, Cursor> {
        private LocDAO mAsyncTaskDao;

        getAllPointsInOneTripAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Cursor doInBackground(Integer... tripId) {
            Log.i("MyMapRepository", "Retieve All points in one trip");
            return mAsyncTaskDao.retrieveAllPointsInOneTrip(tripId[0]);
        }
    }
}
