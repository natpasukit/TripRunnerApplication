package uk.ac.shef.oak.com6510.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import java.io.File;

import uk.ac.shef.oak.com6510.Databases.PhotoEntity;
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
                PhotoViewModel photoViewModel = new PhotoViewModel(getApplication());
                PhotoEntity photoEntity = photoViewModel.getLatestPhoto();
                ImageView imageView = findViewById(R.id.sampleImageView);
                File file = new File(photoEntity.photoFileDirectory);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
        });
    }

}
