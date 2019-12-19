package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(currentPhotoPath));
        File f = new File(this.currentPhotoPath);
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

    /**
     * Get the latest image bitmap photo from path in database then rescale it into imageView size
     * Return original bitmap , return null if context application was not set
     *
     * @param imageView
     * @return Bitmap
     */
    public Bitmap getLatestBitmapPhoto(ImageView imageView) {
        if (this.application != null) {
            // Get imageView size
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            // Set dimension of bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            if (targetH != 0 && targetW != 0) {
                // In-case imageView is wrapper
                bmOptions.inJustDecodeBounds = true;
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Set image scale factor
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Set decoder options
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(this.photoRepository.getLatestPhotoInfo().getPhotoFileDirectory(), bmOptions);
            return bitmap;
        } else {
            return null;
        }
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public static Bitmap getDecodedScaleImage(Application application, ImageView imageView, String imagePath) {
        if (application != null) {
            // Get imageView size
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            // Set dimension of bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            if (targetH != 0 && targetW != 0) {
                // In-case imageView is wrapper
                bmOptions.inJustDecodeBounds = true;
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Set image scale factor
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Set decoder options
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
            return bitmap;
        } else {
            return null;
        }
    }
}
