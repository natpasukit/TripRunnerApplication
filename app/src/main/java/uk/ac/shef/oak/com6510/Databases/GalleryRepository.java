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
     * get all the photot data
     *
     * @return a cursor to photot data return null when there is nothing in the database
     */
    public Cursor getAllPhotoInformation(){
        try {
            Cursor mCursor = new GalleryRepository.getAllPhotoAsyncTask(myPhotoDAO).execute().get();
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
    private static class getAllPhotoAsyncTask extends AsyncTask<Void, Void, Cursor> {
        private PhotoDAO mAsyncTaskDao;

        getAllPhotoAsyncTask(PhotoDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Cursor doInBackground(Void... URL) {
            Log.i("MyMapRepository", "Retieve All Photo");
            return mAsyncTaskDao.retrieveAllPhotoInfo();
        }
    }
}
