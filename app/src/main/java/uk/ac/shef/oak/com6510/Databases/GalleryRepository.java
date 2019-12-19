package uk.ac.shef.oak.com6510.Databases;

import android.app.Application;

import androidx.lifecycle.ViewModel;

public class GalleryRepository extends ViewModel {
    private final PhotoDAO myPhotoDAO;

    public GalleryRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        myPhotoDAO = db.photoDAO();
    }
}
