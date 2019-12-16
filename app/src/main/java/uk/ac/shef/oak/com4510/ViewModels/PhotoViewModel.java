package uk.ac.shef.oak.com4510.ViewModels;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoViewModel {
    String currentPhotoPath;
    Context context;
    public PhotoViewModel(Context context) {
        this.context = context;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        System.out.println(timeStamp);
        System.out.println(imageFileName);
        System.out.println(storageDir);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        System.out.println(currentPhotoPath);
        return image;
    }
}
