package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        // Get intent from image Activity
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        // Get image view layout
        ImageView imageView = (ImageView) findViewById(R.id.fullImageView);
        // Create photo
        Bitmap photoBitmap = PhotoViewModel.getDecodedScaleImage(getApplication(), imageView, imagePath);
        // Bind photo to view
        if (photoBitmap != null) {
            imageView.setImageBitmap(photoBitmap);
        } else {
            Intent intentBack = new Intent(getApplicationContext(), ImageActivity.class);
            intentBack.putExtra("error", true);
            startActivity(intent);
            finish();
        }
    }
}
