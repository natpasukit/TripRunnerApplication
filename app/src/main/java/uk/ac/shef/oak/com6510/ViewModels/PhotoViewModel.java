package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.ac.shef.oak.com6510.Databases.MapRepository;
import uk.ac.shef.oak.com6510.Databases.PhotoEntity;
import uk.ac.shef.oak.com6510.Databases.PhotoRepository;
import uk.ac.shef.oak.com6510.Models.PhotoModel;

public class PhotoViewModel {
    private PhotoRepository photoRepository;
    private String currentPhotoPath;
    private Context context;
    private File image;
    private Application application = null;

    public PhotoViewModel(Context context) {
        this.context = context;
    }

    public PhotoViewModel(Application application) {
        super();
        this.application = application;
        this.photoRepository = new PhotoRepository(application);
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        galleryAddPic();
        return image;
    }

    public void saveImageToDb(Application application, String photoPath, int lastTripId, int lastStopId) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        PhotoModel photoModel = new PhotoModel(application, photoPath, timeStamp, lastTripId, lastStopId);
        photoModel.insertPhotoToDb();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);
    }

    public PhotoEntity getLatestPhoto() {
        if (this.application != null) {
            return this.photoRepository.getLatestPhotoInfo();
        } else
            return null;
    }
}
