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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.ac.shef.oak.com6510.Models.PhotoModel;

public class PhotoViewModel {
    private String currentPhotoPath;
    private Context context;
    private File image;
    private Application application = null;

    /**
     * Create photoViewModel with current activity context.
     *
     * @param context Context, context of this activity
     */
    public PhotoViewModel(Context context) {
        this.context = context;
    }

    /**
     * Pre create image file path with unique name, suffix and prefix.
     *
     * @return File image that able to create path to with generated unique name.
     * @throws IOException when the application cannot create folder and files.
     */
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

    /**
     * Save image path to room database with current stopId and tripId to identify location and trip of this picture.
     *
     * @param application current application activity.
     * @param photoPath   String absolute string path to image that this application can access to in external directory.
     * @param lastTripId  Integer, current trip id
     * @param lastStopId  Integer, current stop id
     */
    public void saveImageToDb(Application application, String photoPath, int lastTripId, int lastStopId) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        PhotoModel photoModel = new PhotoModel(application, photoPath, timeStamp, lastTripId, lastStopId);
        photoModel.insertPhotoToDb();
    }

    /**
     * Broadcast this image intent into gallery to allow other application to access to this image URI
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(currentPhotoPath));
        File f = new File(this.currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Get current photo in the model path.
     *
     * @return String current latest photo path.
     */
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    /**
     * Decode target image path according to imageView size, If possible this will try to rescale the image to fit target view.
     * This increase efficient and ease of use in bitmap. (Decrease memory consumption of jpg to bitmap)
     *
     * @param application Application current context of this application activity.
     * @param imageView   ImageView to fit this image into. (User might want to set fit XY in layout to fit the picture in other scale of 3:4)
     * @param imagePath   String absolute path of this picture image
     * @return Bitmap of this picture that was scaled, return on failure.
     */
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
