package uk.ac.shef.oak.com4510.Databases;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class PhotoRepository extends ViewModel {
    private final PhotoDAO myPhotoDAO;

    public PhotoRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myPhotoDAO = db.photoDAO();
    }

    public void insertOnePhotoData(@NonNull PhotoEntity photoEntity){
        new insertPhotoAsyncTask(myPhotoDAO).execute(photoEntity);
    }

    private static class insertPhotoAsyncTask extends AsyncTask<PhotoEntity, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private LiveData<LocAndSensorData> numberData;

        insertPhotoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PhotoEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyPhotoRepository", params[0].toString());
            return null;
        }
    }

}
