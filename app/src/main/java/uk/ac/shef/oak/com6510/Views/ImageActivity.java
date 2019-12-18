package uk.ac.shef.oak.com6510.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = findViewById(R.id.sampleImageView);
                PhotoViewModel photoViewModel = new PhotoViewModel(getApplication());
                Bitmap bitmap = photoViewModel.getLatestBitmapPhoto(imageView);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }

}
