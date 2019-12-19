package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

/**
 * define a repository to get query from the table
 */
public class PhotoRepository extends ViewModel {
    private final PhotoDAO myPhotoDAO;

    /**
     * init the repository
     * @param application get the current application
     */
    public PhotoRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myPhotoDAO = db.photoDAO();
    }

    /**
     * insert a new row into the photo table
     * @param photoEntity a new line of photo data
     */
    public void insertOnePhotoData(@NonNull PhotoEntity photoEntity) {
        new insertPhotoAsyncTask(myPhotoDAO).execute(photoEntity);
    }

    /**
     * get the latest photo info
     * @return an object of PhotoEntity
     * @throws Exception on error
     */
    public PhotoEntity getLatestPhotoInfo() {
        try {
            return new getLatestPhotoInfoAsyncTask(myPhotoDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * the internal class for Async task to insert a row
     */
    private static class insertPhotoAsyncTask extends AsyncTask<PhotoEntity, Void, Void> {
        private PhotoDAO mAsyncTaskDao;
        private LiveData<LocAndSensorData> numberData;

        insertPhotoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         * @param params a object of photoEntity
         * @return null
         */
        @Override
        protected Void doInBackground(final PhotoEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyPhotoRepository", params[0].toString());
            return null;
        }
    }

    /**
     * the internal class for Async task to get the latest photo data
     */
    private static class getLatestPhotoInfoAsyncTask extends AsyncTask<Void, Void, PhotoEntity> {
        private PhotoDAO mAsyncTaskDao;

        getLatestPhotoInfoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         * @param voids
         * @return a object of PhotoEntity
         */
        @Override
        protected PhotoEntity doInBackground(Void... voids) {
            return mAsyncTaskDao.getLatestPhotoInfo();
        }
    }
}
