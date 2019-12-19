package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

public class GalleryRepository extends ViewModel {
    private final PhotoDAO myPhotoDAO;

    public GalleryRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myPhotoDAO = db.photoDAO();
    }

    public Cursor getAllTripName(){
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
