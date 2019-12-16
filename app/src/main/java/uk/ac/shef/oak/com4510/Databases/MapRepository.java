package uk.ac.shef.oak.com4510.Databases;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MapRepository extends ViewModel {
    private final LocDAO myLocDao;

    public MapRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myLocDao = db.myLocDao();
    }

    public void insertOneData(@NonNull LocAndSensorData locAndSensorData) {
        new insertLocAsyncTask(myLocDao).execute(locAndSensorData);
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

}
