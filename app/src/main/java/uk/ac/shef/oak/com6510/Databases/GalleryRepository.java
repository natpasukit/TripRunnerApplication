package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

/**
 * Define a repository for gallery activity
 * to use AsyncTask to get all the photo info
 *
 * getAllPhotoInformation is use to get all data
 */

public class GalleryRepository extends ViewModel {
    private final PhotoDAO myPhotoDAO;

    /**
     * init the new gallery repository
     *
     * @param application get current application
     */
    public GalleryRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myPhotoDAO = db.photoDAO();
    }

    /**
     * This function create a Async Task to
     * get all the photot data with order
     *
     * @return a cursor to photot data return null when there is nothing in the database
     */
    public Cursor getAllPhotoInOneTripformation(int order){
        try {
            Cursor mCursor = new GalleryRepository.getAllPhotoAsyncTask(myPhotoDAO).execute(order).get();
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

    /**
     * This function create a Async Task to
     * get all the photot data in one trip
     *
     * @return a cursor to photo data return null when there is nothing in the database
     */
    public Cursor getAllPictureInformation(int tripId){
        try {
            Cursor mCursor = new GalleryRepository.getAllPictureInOneTripAsyncTask(myPhotoDAO).execute(tripId).get();
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

    /**
     * The internal class to do the AsyncTask
     */
    private static class getAllPhotoAsyncTask extends AsyncTask<Integer, Void, Cursor> {
        private PhotoDAO mAsyncTaskDao;

        getAllPhotoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         * @param parm use to decide the order, 1 is for ascending ,-1 is for descending
         * @return a cursor to photot data return null when there is nothing in the database
         */
        @Override
        protected Cursor doInBackground(Integer... parm) {
            Log.i("MyMapRepository", "Retieve All Photo");
            if(parm[0] == 1)
                return mAsyncTaskDao.retrieveAllPhotoInfoASC();
            else
                return mAsyncTaskDao.retrieveAllPhotoInfoDESC();
        }
    }

    /**
     * The internal class to do the AsyncTask to retrieve all the picture in one trip
     */
    private static class getAllPictureInOneTripAsyncTask extends AsyncTask<Integer, Void, Cursor> {
        private PhotoDAO mAsyncTaskDao;

        getAllPictureInOneTripAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * async task
         * @param parm use to decide which trip to retrieve
         * @return a cursor to photot data return null when there is nothing in the database
         */
        @Override
        protected Cursor doInBackground(Integer... parm) {
            Log.i("MyMapRepository", "Retieve All Photo in one trip");
            return mAsyncTaskDao.retrieveAllPictureInOneTrip(parm[0]);
        }
    }
}
