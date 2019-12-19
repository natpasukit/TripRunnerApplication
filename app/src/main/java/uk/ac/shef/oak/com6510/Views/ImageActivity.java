package uk.ac.shef.oak.com6510.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.ImageViewAdapter;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

public class ImageActivity extends AppCompatActivity {
    private ImageViewAdapter myImageViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        int tripId = intent.getIntExtra("tripId",-1);

        myImageViewAdapter = new ImageViewAdapter(getApplication(),this,tripId);
    }
}
