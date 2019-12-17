package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

    public int getLatestTripId(){
        try {
            LocAndSensorData locAndSensorData = new getLocAsyncTask(myLocDao).execute().get();
            if(locAndSensorData != null)
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

    private static class getLocAsyncTask extends AsyncTask<Void, Void, LocAndSensorData> {
        private LocDAO mAsyncTaskDao;

        getLocAsyncTask(LocDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LocAndSensorData doInBackground(Void... URL) {
            Log.i("MyMapRepository","Retieve");
            return mAsyncTaskDao.retrieveLatestTripData();
        }
    }

}
